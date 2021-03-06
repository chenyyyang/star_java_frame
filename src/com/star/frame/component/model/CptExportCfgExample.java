package com.star.frame.component.model;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class CptExportCfgExample {
    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database table HFMIS_GUIYANG.CPT_EXPORT_CFG
     *
     * @mbggenerated Fri Sep 14 16:05:34 CST 2012
     */
    protected String orderByClause;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database table HFMIS_GUIYANG.CPT_EXPORT_CFG
     *
     * @mbggenerated Fri Sep 14 16:05:34 CST 2012
     */
    protected boolean distinct;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database table HFMIS_GUIYANG.CPT_EXPORT_CFG
     *
     * @mbggenerated Fri Sep 14 16:05:34 CST 2012
     */
    protected List<Criteria> oredCriteria;

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table HFMIS_GUIYANG.CPT_EXPORT_CFG
     *
     * @mbggenerated Fri Sep 14 16:05:34 CST 2012
     */
    public CptExportCfgExample() {
        oredCriteria = new ArrayList<Criteria>();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table HFMIS_GUIYANG.CPT_EXPORT_CFG
     *
     * @mbggenerated Fri Sep 14 16:05:34 CST 2012
     */
    public void setOrderByClause(String orderByClause) {
        this.orderByClause = orderByClause;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table HFMIS_GUIYANG.CPT_EXPORT_CFG
     *
     * @mbggenerated Fri Sep 14 16:05:34 CST 2012
     */
    public String getOrderByClause() {
        return orderByClause;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table HFMIS_GUIYANG.CPT_EXPORT_CFG
     *
     * @mbggenerated Fri Sep 14 16:05:34 CST 2012
     */
    public void setDistinct(boolean distinct) {
        this.distinct = distinct;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table HFMIS_GUIYANG.CPT_EXPORT_CFG
     *
     * @mbggenerated Fri Sep 14 16:05:34 CST 2012
     */
    public boolean isDistinct() {
        return distinct;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table HFMIS_GUIYANG.CPT_EXPORT_CFG
     *
     * @mbggenerated Fri Sep 14 16:05:34 CST 2012
     */
    public List<Criteria> getOredCriteria() {
        return oredCriteria;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table HFMIS_GUIYANG.CPT_EXPORT_CFG
     *
     * @mbggenerated Fri Sep 14 16:05:34 CST 2012
     */
    public void or(Criteria criteria) {
        oredCriteria.add(criteria);
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table HFMIS_GUIYANG.CPT_EXPORT_CFG
     *
     * @mbggenerated Fri Sep 14 16:05:34 CST 2012
     */
    public Criteria or() {
        Criteria criteria = createCriteriaInternal();
        oredCriteria.add(criteria);
        return criteria;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table HFMIS_GUIYANG.CPT_EXPORT_CFG
     *
     * @mbggenerated Fri Sep 14 16:05:34 CST 2012
     */
    public Criteria createCriteria() {
        Criteria criteria = createCriteriaInternal();
        if (oredCriteria.size() == 0) {
            oredCriteria.add(criteria);
        }
        return criteria;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table HFMIS_GUIYANG.CPT_EXPORT_CFG
     *
     * @mbggenerated Fri Sep 14 16:05:34 CST 2012
     */
    protected Criteria createCriteriaInternal() {
        Criteria criteria = new Criteria();
        return criteria;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table HFMIS_GUIYANG.CPT_EXPORT_CFG
     *
     * @mbggenerated Fri Sep 14 16:05:34 CST 2012
     */
    public void clear() {
        oredCriteria.clear();
        orderByClause = null;
        distinct = false;
    }

    /**
     * This class was generated by MyBatis Generator.
     * This class corresponds to the database table HFMIS_GUIYANG.CPT_EXPORT_CFG
     *
     * @mbggenerated Fri Sep 14 16:05:34 CST 2012
     */
    protected abstract static class GeneratedCriteria {
        protected List<Criterion> criteria;

        protected GeneratedCriteria() {
            super();
            criteria = new ArrayList<Criterion>();
        }

        public boolean isValid() {
            return criteria.size() > 0;
        }

        public List<Criterion> getAllCriteria() {
            return criteria;
        }

        public List<Criterion> getCriteria() {
            return criteria;
        }

        protected void addCriterion(String condition) {
            if (condition == null) {
                throw new RuntimeException("Value for condition cannot be null");
            }
            criteria.add(new Criterion(condition));
        }

        protected void addCriterion(String condition, Object value, String property) {
            if (value == null) {
                throw new RuntimeException("Value for " + property + " cannot be null");
            }
            criteria.add(new Criterion(condition, value));
        }

        protected void addCriterion(String condition, Object value1, Object value2, String property) {
            if (value1 == null || value2 == null) {
                throw new RuntimeException("Between values for " + property + " cannot be null");
            }
            criteria.add(new Criterion(condition, value1, value2));
        }

        public Criteria andAppIdIsNull() {
            addCriterion("APP_ID is null");
            return (Criteria) this;
        }

        public Criteria andAppIdIsNotNull() {
            addCriterion("APP_ID is not null");
            return (Criteria) this;
        }

        public Criteria andAppIdEqualTo(String value) {
            addCriterion("APP_ID =", value, "appId");
            return (Criteria) this;
        }

        public Criteria andAppIdNotEqualTo(String value) {
            addCriterion("APP_ID <>", value, "appId");
            return (Criteria) this;
        }

        public Criteria andAppIdGreaterThan(String value) {
            addCriterion("APP_ID >", value, "appId");
            return (Criteria) this;
        }

        public Criteria andAppIdGreaterThanOrEqualTo(String value) {
            addCriterion("APP_ID >=", value, "appId");
            return (Criteria) this;
        }

        public Criteria andAppIdLessThan(String value) {
            addCriterion("APP_ID <", value, "appId");
            return (Criteria) this;
        }

        public Criteria andAppIdLessThanOrEqualTo(String value) {
            addCriterion("APP_ID <=", value, "appId");
            return (Criteria) this;
        }

        public Criteria andAppIdLike(String value) {
            addCriterion("APP_ID like", value, "appId");
            return (Criteria) this;
        }

        public Criteria andAppIdNotLike(String value) {
            addCriterion("APP_ID not like", value, "appId");
            return (Criteria) this;
        }

        public Criteria andAppIdIn(List<String> values) {
            addCriterion("APP_ID in", values, "appId");
            return (Criteria) this;
        }

        public Criteria andAppIdNotIn(List<String> values) {
            addCriterion("APP_ID not in", values, "appId");
            return (Criteria) this;
        }

        public Criteria andAppIdBetween(String value1, String value2) {
            addCriterion("APP_ID between", value1, value2, "appId");
            return (Criteria) this;
        }

        public Criteria andAppIdNotBetween(String value1, String value2) {
            addCriterion("APP_ID not between", value1, value2, "appId");
            return (Criteria) this;
        }

        public Criteria andIdIsNull() {
            addCriterion("ID is null");
            return (Criteria) this;
        }

        public Criteria andIdIsNotNull() {
            addCriterion("ID is not null");
            return (Criteria) this;
        }

        public Criteria andIdEqualTo(BigDecimal value) {
            addCriterion("ID =", value, "id");
            return (Criteria) this;
        }

        public Criteria andIdNotEqualTo(BigDecimal value) {
            addCriterion("ID <>", value, "id");
            return (Criteria) this;
        }

        public Criteria andIdGreaterThan(BigDecimal value) {
            addCriterion("ID >", value, "id");
            return (Criteria) this;
        }

        public Criteria andIdGreaterThanOrEqualTo(BigDecimal value) {
            addCriterion("ID >=", value, "id");
            return (Criteria) this;
        }

        public Criteria andIdLessThan(BigDecimal value) {
            addCriterion("ID <", value, "id");
            return (Criteria) this;
        }

        public Criteria andIdLessThanOrEqualTo(BigDecimal value) {
            addCriterion("ID <=", value, "id");
            return (Criteria) this;
        }

        public Criteria andIdIn(List<BigDecimal> values) {
            addCriterion("ID in", values, "id");
            return (Criteria) this;
        }

        public Criteria andIdNotIn(List<BigDecimal> values) {
            addCriterion("ID not in", values, "id");
            return (Criteria) this;
        }

        public Criteria andIdBetween(BigDecimal value1, BigDecimal value2) {
            addCriterion("ID between", value1, value2, "id");
            return (Criteria) this;
        }

        public Criteria andIdNotBetween(BigDecimal value1, BigDecimal value2) {
            addCriterion("ID not between", value1, value2, "id");
            return (Criteria) this;
        }

        public Criteria andNameIsNull() {
            addCriterion("NAME is null");
            return (Criteria) this;
        }

        public Criteria andNameIsNotNull() {
            addCriterion("NAME is not null");
            return (Criteria) this;
        }

        public Criteria andNameEqualTo(String value) {
            addCriterion("NAME =", value, "name");
            return (Criteria) this;
        }

        public Criteria andNameNotEqualTo(String value) {
            addCriterion("NAME <>", value, "name");
            return (Criteria) this;
        }

        public Criteria andNameGreaterThan(String value) {
            addCriterion("NAME >", value, "name");
            return (Criteria) this;
        }

        public Criteria andNameGreaterThanOrEqualTo(String value) {
            addCriterion("NAME >=", value, "name");
            return (Criteria) this;
        }

        public Criteria andNameLessThan(String value) {
            addCriterion("NAME <", value, "name");
            return (Criteria) this;
        }

        public Criteria andNameLessThanOrEqualTo(String value) {
            addCriterion("NAME <=", value, "name");
            return (Criteria) this;
        }

        public Criteria andNameLike(String value) {
            addCriterion("NAME like", value, "name");
            return (Criteria) this;
        }

        public Criteria andNameNotLike(String value) {
            addCriterion("NAME not like", value, "name");
            return (Criteria) this;
        }

        public Criteria andNameIn(List<String> values) {
            addCriterion("NAME in", values, "name");
            return (Criteria) this;
        }

        public Criteria andNameNotIn(List<String> values) {
            addCriterion("NAME not in", values, "name");
            return (Criteria) this;
        }

        public Criteria andNameBetween(String value1, String value2) {
            addCriterion("NAME between", value1, value2, "name");
            return (Criteria) this;
        }

        public Criteria andNameNotBetween(String value1, String value2) {
            addCriterion("NAME not between", value1, value2, "name");
            return (Criteria) this;
        }

        public Criteria andExportNameIsNull() {
            addCriterion("EXPORT_NAME is null");
            return (Criteria) this;
        }

        public Criteria andExportNameIsNotNull() {
            addCriterion("EXPORT_NAME is not null");
            return (Criteria) this;
        }

        public Criteria andExportNameEqualTo(String value) {
            addCriterion("EXPORT_NAME =", value, "exportName");
            return (Criteria) this;
        }

        public Criteria andExportNameNotEqualTo(String value) {
            addCriterion("EXPORT_NAME <>", value, "exportName");
            return (Criteria) this;
        }

        public Criteria andExportNameGreaterThan(String value) {
            addCriterion("EXPORT_NAME >", value, "exportName");
            return (Criteria) this;
        }

        public Criteria andExportNameGreaterThanOrEqualTo(String value) {
            addCriterion("EXPORT_NAME >=", value, "exportName");
            return (Criteria) this;
        }

        public Criteria andExportNameLessThan(String value) {
            addCriterion("EXPORT_NAME <", value, "exportName");
            return (Criteria) this;
        }

        public Criteria andExportNameLessThanOrEqualTo(String value) {
            addCriterion("EXPORT_NAME <=", value, "exportName");
            return (Criteria) this;
        }

        public Criteria andExportNameLike(String value) {
            addCriterion("EXPORT_NAME like", value, "exportName");
            return (Criteria) this;
        }

        public Criteria andExportNameNotLike(String value) {
            addCriterion("EXPORT_NAME not like", value, "exportName");
            return (Criteria) this;
        }

        public Criteria andExportNameIn(List<String> values) {
            addCriterion("EXPORT_NAME in", values, "exportName");
            return (Criteria) this;
        }

        public Criteria andExportNameNotIn(List<String> values) {
            addCriterion("EXPORT_NAME not in", values, "exportName");
            return (Criteria) this;
        }

        public Criteria andExportNameBetween(String value1, String value2) {
            addCriterion("EXPORT_NAME between", value1, value2, "exportName");
            return (Criteria) this;
        }

        public Criteria andExportNameNotBetween(String value1, String value2) {
            addCriterion("EXPORT_NAME not between", value1, value2, "exportName");
            return (Criteria) this;
        }

        public Criteria andServiceNameIsNull() {
            addCriterion("SERVICE_NAME is null");
            return (Criteria) this;
        }

        public Criteria andServiceNameIsNotNull() {
            addCriterion("SERVICE_NAME is not null");
            return (Criteria) this;
        }

        public Criteria andServiceNameEqualTo(String value) {
            addCriterion("SERVICE_NAME =", value, "serviceName");
            return (Criteria) this;
        }

        public Criteria andServiceNameNotEqualTo(String value) {
            addCriterion("SERVICE_NAME <>", value, "serviceName");
            return (Criteria) this;
        }

        public Criteria andServiceNameGreaterThan(String value) {
            addCriterion("SERVICE_NAME >", value, "serviceName");
            return (Criteria) this;
        }

        public Criteria andServiceNameGreaterThanOrEqualTo(String value) {
            addCriterion("SERVICE_NAME >=", value, "serviceName");
            return (Criteria) this;
        }

        public Criteria andServiceNameLessThan(String value) {
            addCriterion("SERVICE_NAME <", value, "serviceName");
            return (Criteria) this;
        }

        public Criteria andServiceNameLessThanOrEqualTo(String value) {
            addCriterion("SERVICE_NAME <=", value, "serviceName");
            return (Criteria) this;
        }

        public Criteria andServiceNameLike(String value) {
            addCriterion("SERVICE_NAME like", value, "serviceName");
            return (Criteria) this;
        }

        public Criteria andServiceNameNotLike(String value) {
            addCriterion("SERVICE_NAME not like", value, "serviceName");
            return (Criteria) this;
        }

        public Criteria andServiceNameIn(List<String> values) {
            addCriterion("SERVICE_NAME in", values, "serviceName");
            return (Criteria) this;
        }

        public Criteria andServiceNameNotIn(List<String> values) {
            addCriterion("SERVICE_NAME not in", values, "serviceName");
            return (Criteria) this;
        }

        public Criteria andServiceNameBetween(String value1, String value2) {
            addCriterion("SERVICE_NAME between", value1, value2, "serviceName");
            return (Criteria) this;
        }

        public Criteria andServiceNameNotBetween(String value1, String value2) {
            addCriterion("SERVICE_NAME not between", value1, value2, "serviceName");
            return (Criteria) this;
        }

        public Criteria andColumnInfoIsNull() {
            addCriterion("COLUMN_INFO is null");
            return (Criteria) this;
        }

        public Criteria andColumnInfoIsNotNull() {
            addCriterion("COLUMN_INFO is not null");
            return (Criteria) this;
        }

        public Criteria andColumnInfoEqualTo(String value) {
            addCriterion("COLUMN_INFO =", value, "columnInfo");
            return (Criteria) this;
        }

        public Criteria andColumnInfoNotEqualTo(String value) {
            addCriterion("COLUMN_INFO <>", value, "columnInfo");
            return (Criteria) this;
        }

        public Criteria andColumnInfoGreaterThan(String value) {
            addCriterion("COLUMN_INFO >", value, "columnInfo");
            return (Criteria) this;
        }

        public Criteria andColumnInfoGreaterThanOrEqualTo(String value) {
            addCriterion("COLUMN_INFO >=", value, "columnInfo");
            return (Criteria) this;
        }

        public Criteria andColumnInfoLessThan(String value) {
            addCriterion("COLUMN_INFO <", value, "columnInfo");
            return (Criteria) this;
        }

        public Criteria andColumnInfoLessThanOrEqualTo(String value) {
            addCriterion("COLUMN_INFO <=", value, "columnInfo");
            return (Criteria) this;
        }

        public Criteria andColumnInfoLike(String value) {
            addCriterion("COLUMN_INFO like", value, "columnInfo");
            return (Criteria) this;
        }

        public Criteria andColumnInfoNotLike(String value) {
            addCriterion("COLUMN_INFO not like", value, "columnInfo");
            return (Criteria) this;
        }

        public Criteria andColumnInfoIn(List<String> values) {
            addCriterion("COLUMN_INFO in", values, "columnInfo");
            return (Criteria) this;
        }

        public Criteria andColumnInfoNotIn(List<String> values) {
            addCriterion("COLUMN_INFO not in", values, "columnInfo");
            return (Criteria) this;
        }

        public Criteria andColumnInfoBetween(String value1, String value2) {
            addCriterion("COLUMN_INFO between", value1, value2, "columnInfo");
            return (Criteria) this;
        }

        public Criteria andColumnInfoNotBetween(String value1, String value2) {
            addCriterion("COLUMN_INFO not between", value1, value2, "columnInfo");
            return (Criteria) this;
        }
    }

    /**
     * This class was generated by MyBatis Generator.
     * This class corresponds to the database table HFMIS_GUIYANG.CPT_EXPORT_CFG
     *
     * @mbggenerated do_not_delete_during_merge Fri Sep 14 16:05:34 CST 2012
     */
    public static class Criteria extends GeneratedCriteria {

        protected Criteria() {
            super();
        }
    }

    /**
     * This class was generated by MyBatis Generator.
     * This class corresponds to the database table HFMIS_GUIYANG.CPT_EXPORT_CFG
     *
     * @mbggenerated Fri Sep 14 16:05:34 CST 2012
     */
    public static class Criterion {
        private String condition;

        private Object value;

        private Object secondValue;

        private boolean noValue;

        private boolean singleValue;

        private boolean betweenValue;

        private boolean listValue;

        private String typeHandler;

        public String getCondition() {
            return condition;
        }

        public Object getValue() {
            return value;
        }

        public Object getSecondValue() {
            return secondValue;
        }

        public boolean isNoValue() {
            return noValue;
        }

        public boolean isSingleValue() {
            return singleValue;
        }

        public boolean isBetweenValue() {
            return betweenValue;
        }

        public boolean isListValue() {
            return listValue;
        }

        public String getTypeHandler() {
            return typeHandler;
        }

        protected Criterion(String condition) {
            super();
            this.condition = condition;
            this.typeHandler = null;
            this.noValue = true;
        }

        protected Criterion(String condition, Object value, String typeHandler) {
            super();
            this.condition = condition;
            this.value = value;
            this.typeHandler = typeHandler;
            if (value instanceof List<?>) {
                this.listValue = true;
            } else {
                this.singleValue = true;
            }
        }

        protected Criterion(String condition, Object value) {
            this(condition, value, null);
        }

        protected Criterion(String condition, Object value, Object secondValue, String typeHandler) {
            super();
            this.condition = condition;
            this.value = value;
            this.secondValue = secondValue;
            this.typeHandler = typeHandler;
            this.betweenValue = true;
        }

        protected Criterion(String condition, Object value, Object secondValue) {
            this(condition, value, secondValue, null);
        }
    }
}