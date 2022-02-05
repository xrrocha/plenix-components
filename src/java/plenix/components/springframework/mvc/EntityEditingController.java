package plenix.components.springframework.mvc;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.validation.BindException;
import org.springframework.web.bind.ServletRequestDataBinder;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.multiaction.MultiActionController;
import plenix.components.beans.PropertyEditorFactory;
import plenix.components.beans.PropertyEditorFactoryRegistry;
import plenix.components.dao.Dao;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.beans.*;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class EntityEditingController extends MultiActionController {
    private Dao dao;
    private Class entityClass;
    private String entityName;
    private Class idPropertyEditorClass;

    private String listUrl = "list";
    private String addUrl = "addForm";
    private String updateUrl = "updateForm";

    private String modelName = "model";
    private String errorName = "errors";

    private String collectionName = "records";

    private String entityErrorCode = "entityErrorCode";

    private String actionParameterName = "action";
    private String addActionName = "Add";
    private String cancelActionName = "Cancel";
    private String updateActionName = "Update";
    private String removeActionName = "Remove";

    private String idPropertyName = "id";

    private PropertyEditorFactoryRegistry propertyEditorFactoryRegistry;

    protected final Log logger = LogFactory.getLog(getClass());
    private Map<Class, PropertyEditorFactory> propertyFactories;

    public ModelAndView list(HttpServletRequest request,
                             HttpServletResponse response)
        throws Exception
    {
        return new ModelAndView(viewName(getListUrl()),
                                getCollectionName(),
                                getDao().retrieve(getEntityClass()));
    }

    public ModelAndView addForm(HttpServletRequest request,
                                HttpServletResponse response)
        throws Exception
    {
        return new ModelAndView(viewName(getAddUrl()));
    }

    public ModelAndView addAction(HttpServletRequest request,
                                  HttpServletResponse response)
        throws Exception
    {
        String action = request.getParameter(getActionParameterName());
        if (getCancelActionName().equals(action)) {
            return new ModelAndView(viewName(getListUrl()),
                                    getCollectionName(),
                                    getDao().retrieve(getEntityClass()));
        }
            if (getAddActionName().equals(action)) {
            Object entity = newEntityInstance();
            ServletRequestDataBinder binder = createBinder(entity);
            BindException errors = binder.getErrors();

            Map<String, Object> model = new HashMap<String, Object>();
            model.put(getEntityName(), entity);

            try {
                binder.bind(request);
                getDao().store(entity);
                return new ModelAndView(viewName(getUpdateUrl()),
                                        getModelName(), model);
            } catch (Exception e) {
                logger.warn(e.toString());
                errors.reject(getEntityErrorCode(), e.getMessage());
                model.put(getErrorName(), errors);
                return new ModelAndView(viewName(getAddUrl()),
                                        getModelName(), model);
            }
        }
        throw new IllegalArgumentException("Unrecognized add action: " + action);
    }

    public ModelAndView updateForm(HttpServletRequest request,
                                   HttpServletResponse response)
        throws Exception
    {
        Serializable id = getIdFromRequest(request);
        Map<String, Object> modelo = new HashMap<String, Object>();
        modelo.put(getEntityName(), getDao().retrieve(getEntityClass(), id));
        return new ModelAndView(viewName(getUpdateUrl()),
                                getModelName(), modelo);
    }

    public ModelAndView updateAction(HttpServletRequest request,
                                     HttpServletResponse response)
        throws Exception
    {
        String action = request.getParameter(getActionParameterName());

        if (getCancelActionName().equals(action)) {
            return new ModelAndView(viewName(getListUrl()),
                                    getCollectionName(),
                                    getDao().retrieve(getEntityClass()));
        }

        Map<String, Object> model = new HashMap<String, Object>();

        Object entity = getDao().retrieve(getEntityClass(), getIdFromRequest(request));
        model.put(getEntityName(), entity);

        ServletRequestDataBinder binder = createBinder(entity);
        BindException errors = binder.getErrors();

        if (getUpdateActionName().equals(action)) {
            try {
                binder.bind(request);
                getDao().store(entity);
            } catch (Exception e) {
                logger.warn(e.toString());
                errors.reject(getEntityErrorCode(), e.getMessage());
                model.put(getErrorName(), errors);
            }
            return new ModelAndView(viewName(getUpdateUrl()),
                                    getModelName(), model);
        }

        if (getRemoveActionName().equals(action)) {
            try {
                getDao().remove(entity);
                return new ModelAndView(viewName(getListUrl()),
                                        getCollectionName(),
                                        getDao().retrieve(getEntityClass()));
            } catch (Exception e) {
                logger.warn(e.toString());
                errors.reject(getEntityErrorCode(), e.getMessage());
                model.put(getErrorName(), errors);
                return new ModelAndView(viewName(getUpdateUrl()),
                                        getModelName(), model);
            }
        }

        throw new IllegalArgumentException("Unrecognized udpate action: " + action);
    }

    private String viewName(String suffux) {
        return getEntityName() + "/" + suffux;
    }

    private ServletRequestDataBinder createBinder(Object entity) throws IntrospectionException {
        ServletRequestDataBinder binder = new ServletRequestDataBinder(entity, getEntityName());
        for (Class clazz: getPropertyFactories().keySet()) {
            binder.registerCustomEditor(clazz, propertyFactories.get(clazz).newInstance());
        }
        return binder;
    }

    private Map<Class, PropertyEditorFactory> getPropertyFactories()  throws IntrospectionException {
        if (propertyFactories == null) {
            propertyFactories = new HashMap<Class, PropertyEditorFactory>();
            BeanInfo beanInfo = Introspector.getBeanInfo(getEntityClass());
            for(PropertyDescriptor descriptor: beanInfo.getPropertyDescriptors()) {
                Class propertyClass = descriptor.getPropertyType();
                if (!propertyFactories.containsKey(propertyClass)) {
                    PropertyEditorFactory factory = getPropertyEditorFactoryRegistry().getFactoryFor(propertyClass);
                    if (factory != null) {
                        propertyFactories.put(propertyClass, factory);
                    }
                }
            }
        }
        return propertyFactories;
    }

    // TODO Leverage setProperty editor factories
    private Serializable getIdFromRequest(HttpServletRequest request) throws InstantiationException, IllegalAccessException{
        String idParameter = request.getParameter(getIdPropertyName());
        Serializable id = idParameter;
        if (getIdPropertyEditorClass() != null) {
            PropertyEditor idPropertyEditor = (PropertyEditor) getIdPropertyEditorClass().newInstance();
            idPropertyEditor.setAsText(idParameter);
            id = (Serializable) idPropertyEditor.getValue();
        }
        return id;
    }

    private Object newEntityInstance() throws Exception {
        return (Object) this.getEntityClass().newInstance();
    }

    /**
     * @param listUrl The listUrl to set.
     */
    public void setListUrl(String listUrl) {
        this.listUrl = listUrl;
    }

    /**
     * @return Returns the listUrl.
     */
    public String getListUrl() {
        return listUrl;
    }

    /**
     * @param addUrl The addUrl to set.
     */
    public void setAddUrl(String addUrl) {
        this.addUrl = addUrl;
    }

    /**
     * @return Returns the addUrl.
     */
    public String getAddUrl() {
        return addUrl;
    }

    /**
     * @param updateUrl The updateUrl to set.
     */
    public void setUpdateUrl(String updateUrl) {
        this.updateUrl = updateUrl;
    }

    /**
     * @return Returns the updateUrl.
     */
    public String getUpdateUrl() {
        return updateUrl;
    }

    /**
     * @param dao The dao to set.
     */
    public void setDao(Dao dao) {
        this.dao = dao;
    }

    /**
     * @return Returns the dao.
     */
    public Dao getDao() {
        return dao;
    }

    /**
     * @param entityClass The entityClass to set.
     */
    public void setEntityClass(Class entityClass) {
        this.entityClass = entityClass;
    }

    /**
     * @return Returns the entityClass.
     */
    public Class getEntityClass() {
        return entityClass;
    }

    /**
     * @param entityName The entityName to set.
     */
    public void setEntityName(String entityName) {
        this.entityName = entityName;
    }

    /**
     * @return Returns the entityName.
     */
    public String getEntityName() {
        return entityName;
    }

    /**
     * @param collectionName The collectionName to set.
     */
    public void setCollectionName(String collectionName) {
        this.collectionName = collectionName;
    }

    /**
     * @return Returns the collectionName.
     */
    public String getCollectionName() {
        return collectionName;
    }

    /**
     * @param modelName The modelName to set.
     */
    public void setModelName(String modelName) {
        this.modelName = modelName;
    }

    /**
     * @return Returns the modelName.
     */
    public String getModelName() {
        return modelName;
    }

    /**
     * @param errorName The errorName to set.
     */
    public void setErrorName(String errorName) {
        this.errorName = errorName;
    }

    /**
     * @return Returns the errorName.
     */
    public String getErrorName() {
        return errorName;
    }

    /**
     * @param entityErrorCode The entityErrorCode to set.
     */
    public void setEntityErrorCode(String entityErrorCode) {
        this.entityErrorCode = entityErrorCode;
    }

    /**
     * @return Returns the entityErrorCode.
     */
    public String getEntityErrorCode() {
        return entityErrorCode;
    }

    /**
     * @param actionParameterName The actionParameterName to set.
     */
    public void setActionParameterName(String actionParameterName) {
        this.actionParameterName = actionParameterName;
    }

    /**
     * @return Returns the actionParameterName.
     */
    public String getActionParameterName() {
        return actionParameterName;
    }

    /**
     * @param addActionName The addActionName to set.
     */
    public void setAddActionName(String addActionName) {
        this.addActionName = addActionName;
    }

    /**
     * @return Returns the addActionName.
     */
    public String getAddActionName() {
        return addActionName;
    }

    /**
     * @param cancelActionName The cancelActionName to set.
     */
    public void setCancelActionName(String cancelActionName) {
        this.cancelActionName = cancelActionName;
    }

    /**
     * @return Returns the cancelActionName.
     */
    public String getCancelActionName() {
        return cancelActionName;
    }

    /**
     * @param updateActionName The updateActionName to set.
     */
    public void setUpdateActionName(String updateActionName) {
        this.updateActionName = updateActionName;
    }

    /**
     * @return Returns the updateActionName.
     */
    public String getUpdateActionName() {
        return updateActionName;
    }

    /**
     * @param removeActionName The removeActionName to set.
     */
    public void setRemoveActionName(String removeActionName) {
        this.removeActionName = removeActionName;
    }

    /**
     * @return Returns the removeActionName.
     */
    public String getRemoveActionName() {
        return removeActionName;
    }

    /**
     * @param idPropertyName The idPropertyName to set.
     */
    public void setIdPropertyName(String idPropertyName) {
        this.idPropertyName = idPropertyName;
    }

    /**
     * @return Returns the idPropertyName.
     */
    public String getIdPropertyName() {
        return idPropertyName;
    }

    public Class getIdPropertyEditorClass() {
        return idPropertyEditorClass;
    }

    public void setIdPropertyEditorClass(Class idPropertyEditorClass) {
        this.idPropertyEditorClass = idPropertyEditorClass;
    }

    public PropertyEditorFactoryRegistry getPropertyEditorFactoryRegistry() {
        return propertyEditorFactoryRegistry;
    }

    public void setPropertyEditorFactoryRegistry(PropertyEditorFactoryRegistry propertyEditorFactoryRegistry) {
        this.propertyEditorFactoryRegistry = propertyEditorFactoryRegistry;
    }
}
