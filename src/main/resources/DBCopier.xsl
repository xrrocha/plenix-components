<?xml version="1.0"?>

<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
    <xsl:output method="xml" indent="yes"
                doctype-public="-//SPRING//DTD BEAN//EN"
                doctype-system="http://www.springframework.org/dtd/spring-beans.dtd"/>

    <xsl:template match="database-copier">
        <beans>
            <bean id="dbCopier" class="plenix.tools.dbcopier.DBCopier">
                <property name="copiers">
                    <list>
                        <xsl:apply-templates select="copy|include"/>
                    </list>
                </property>
            </bean>
            <xsl:apply-templates select="database"/>
            <bean id="variableExpander" class="plenix.components.variable.DelimitedVariableExpander"/>
            <bean id="resultSet2ColumnValueMapAdapter" class="plenix.components.jdbc.ResultSet2ColumnValueMapAdapter"/>
        </beans>
    </xsl:template>

    <xsl:template match="copy">
        <bean class="plenix.components.copying.CopierImpl">
            <property name="producerFactory">
                <xsl:apply-templates select="from"/>
            </property>
            <property name="consumerFactory">
                <xsl:apply-templates select="to"/>
            </property>
        </bean>
    </xsl:template>

    <xsl:template match="from">
        <bean class="plenix.components.copying.jdbc.JDBCProducerFactory">
            <property name="dataSource">
                <ref local="{@database}DataSource"/>
            </property>
            <property name="queryExecutor">
                <bean class="plenix.components.jdbc.SQLQueryExecutorImpl">
                    <property name="sql">
                        <value>
                            <xsl:value-of select="string(.)"/>
                        </value>
                    </property>
                    <property name="expander">
                        <ref bean="variableExpander"/>
                    </property>
                    <xsl:if test="@fetch-size">
                        <property name="fetchSize">
                            <value>
                                <xsl:value-of select="@fetch-size"/>
                            </value>
                        </property>
                    </xsl:if>
                </bean>
            </property>
        </bean>
    </xsl:template>

    <xsl:template match="to">
        <bean class="plenix.components.copying.adapter.AdaptedConsumerFactory">
            <property name="adapter">
                <ref bean="resultSet2ColumnValueMapAdapter"/>
            </property>
            <property name="delegate">
                <bean class="plenix.components.copying.jdbc.JDBCConsumerFactory">
                    <property name="dataSource">
                        <ref local="{@database}DataSource"/>
                    </property>
                    <xsl:if test="before">
                        <property name="before">
                            <list>
                                <xsl:apply-templates select="before/sql"/>
                            </list>
                        </property>
                    </xsl:if>
                    <property name="forEach">
                        <list>
                            <xsl:apply-templates select="for-each-row/sql"/>
                        </list>
                    </property>
                    <xsl:if test="after">
                        <property name="after">
                            <list>
                                <xsl:apply-templates select="after/sql"/>
                            </list>
                        </property>
                    </xsl:if>
                </bean>
            </property>
        </bean>
    </xsl:template>

    <xsl:template match="sql">
        <bean class="plenix.components.jdbc.SQLExecutorFactoryImpl">
            <property name="expander">
                <ref bean="variableExpander"/>
            </property>
            <property name="sql">
                <value>
                    <xsl:value-of select="string(.)"/>
                </value>
            </property>
            <xsl:if test="@batch-size">
                <property name="batchSize">
                    <value>
                        <xsl:value-of select="@batch-size"/>
                    </value>
                </property>
            </xsl:if>
            <xsl:if test="@ignore-error">
                <property name="ignoreErrors">
                    <value>
                        <xsl:value-of select="@ignore-error"/>
                    </value>
                </property>
            </xsl:if>
        </bean>
    </xsl:template>

    <xsl:template match="database">
        <bean id="{@name}DataSource" class="org.apache.commons.dbcp.BasicDataSource">
            <property name="driverClassName">
                <value>
                    <xsl:value-of select="driver/@class"/>
                </value>
            </property>
            <property name="url">
                <value>
                    <xsl:value-of select="driver/@url"/>
                </value>
            </property>
            <property name="username">
                <value>
                    <xsl:value-of select="user/@name"/>
                </value>
            </property>
            <property name="password">
                <value>
                    <xsl:value-of select="user/@password"/>
                </value>
            </property>
            <property name="defaultAutoCommit">
                <value>
                    <xsl:value-of select="@auto-commit"/>
                </value>
            </property>
        </bean>
    </xsl:template>

    <xsl:template match="include">
        <xsl:apply-templates select="document(@href)"/>
    </xsl:template>
</xsl:stylesheet>
