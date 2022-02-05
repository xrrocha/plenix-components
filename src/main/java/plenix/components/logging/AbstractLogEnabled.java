package plenix.components.logging;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * AbstractLogEnabled.
 */
public abstract class AbstractLogEnabled implements LogEnabled {
    private Log log;

    public AbstractLogEnabled() {
    }

    public void setLog(Log log) {
        this.log = log;
    }

    public Log getLog() {
        if (this.log == null) {
            this.createLog(this.getClass().getName());
        }
        return this.log;
    }

    protected void createLog(String name) {
        try {
            this.log = LogFactory.getLog(name);
        } catch (Exception e) {
            throw new RuntimeException("Can't create log instance: " + name, e);
        }
    }

    protected void debug(Object message) {
        if (this.getLog().isDebugEnabled()) {
            this.getLog().debug(message);
        }
    }

    protected void debug(Object message, Throwable t) {
        if (this.getLog().isDebugEnabled()) {
            this.getLog().debug(message, t);
        }
    }

    protected void error(Object message) {
        if (this.getLog().isErrorEnabled()) {
            this.getLog().error(message);
        }
    }

    protected void error(Object message, Throwable t) {
        if (this.getLog().isErrorEnabled()) {
            this.getLog().error(message, t);
        }
    }

    protected void fatal(Object message) {
        if (this.getLog().isFatalEnabled()) {
            this.getLog().fatal(message);
        }
    }

    protected void fatal(Object message, Throwable t) {
        if (this.getLog().isFatalEnabled()) {
            this.getLog().fatal(message, t);
        }
    }

    protected void info(Object message) {
        if (this.getLog().isInfoEnabled()) {
            this.getLog().info(message);
        }
    }

    protected void info(Object message, Throwable t) {
        if (this.getLog().isInfoEnabled()) {
            this.getLog().info(message, t);
        }
    }

    protected void trace(Object message) {
        if (this.getLog().isTraceEnabled()) {
            this.getLog().trace(message);
        }
    }

    protected void trace(Object message, Throwable t) {
        if (this.getLog().isTraceEnabled()) {
            this.getLog().trace(message, t);
        }
    }

    protected void warn(Object message) {
        if (this.getLog().isWarnEnabled()) {
            this.getLog().warn(message);
        }
    }

    protected void warn(Object message, Throwable t) {
        if (this.getLog().isWarnEnabled()) {
            this.getLog().warn(message, t);
        }
    }
}
