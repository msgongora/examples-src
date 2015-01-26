/**
 * TreeFolder.java
 *

    private java.lang.String type;

    private List<Base> contents;

    private List<CCEntity> entities;

    private List<TreeFolder> folders;

    public TreeFolder()
    {
    }

    public TreeFolder(java.lang.String name, java.lang.String id, java.lang.String type,
            List<Base> contentsName, List<CCEntity> entities, List<TreeFolder> folders)
    {
        super(name);
        this.id = id;
        this.type = type;
        this.contents = contentsName;
        this.entities = entities;
        this.folders = folders;
    }

    /**
     * Gets the id value for this TreeFolder.
     * 
     * @return id
     */
    public java.lang.String getId()
    {
        return id;
    }

    /**
     * Sets the id value for this TreeFolder.
     * 
     * @param id
     */
    public void setId(java.lang.String id)
    {
        this.id = id;
    }

    /**
     * Gets the type value for this TreeFolder.
     * 
     * @return type
     */
    public java.lang.String getType()
    {
        return type;
    }

    /**
     * Sets the type value for this TreeFolder.
     * 
     * @param type
     */
    public void setType(java.lang.String type)
    {
        this.type = type;
    }

    /**
     * Gets the contentsName value for this TreeFolder.
     * 
     * @return contentsName
     */
    public String[] getContentsName()
    {
        int length = contents.size();
        String[] contentsName = new String[length];
        for (int i = 0; i < length; i++)
        {
            contentsName[i] = contents.get(i).getName();
        }
        return contentsName;
    }

    /**
     * Sets the contentsName value for this TreeFolder.
     * 
     * @param contentsName
     */
    public void setContentsName(List<Base> contentsName)
    {
        this.contents = contentsName;
    }

    public Base getContentsName(int i)
    {
        return this.contents.get(i);
    }

    public void setContentsName(int i, Base _value)
    {
        this.contents.set(i, _value);
    }

    /**
     * Gets the entities value for this TreeFolder.
     * 
     * @return entities
     */
    public List<CCEntity> getEntities()
    {
        return entities;
    }

    /**
     * Sets the entities value for this TreeFolder.
     * 
     * @param entities
     */
    public void setEntities(List<CCEntity> entities)
    {
        this.entities = entities;
    }

    public org.eXcriba.repo.webservice.CCEntity getEntities(int i)
    {
        return this.entities.get(i);
    }

    public void setEntities(int i, org.eXcriba.repo.webservice.CCEntity _value)
    {
        this.entities.set(i, _value);
    }

    /**
     * Gets the folders value for this TreeFolder.
     * 
     * @return folders
     */
    public List<TreeFolder> getFolders()
    {
        return folders;
    }

    /**
     * Sets the folders value for this TreeFolder.
     * 
     * @param folders
     */
    public void setFolders(List<TreeFolder> folders)
    {
        this.folders = folders;
    }

    public org.eXcriba.repo.webservice.recordsmanagement.TreeFolder getFolders(int i)
    {
        return this.folders.get(i);
    }

    public void setFolders(int i, org.eXcriba.repo.webservice.recordsmanagement.TreeFolder _value)
    {
        this.folders.set(i, _value);
    }

    // Type metadata
    // private static org.apache.axis.description.TypeDesc typeDesc =
    // new org.apache.axis.description.TypeDesc(TreeFolder.class, true);
    //
    // static {
    // typeDesc.setXmlType(new
    // javax.xml.namespace.QName("http://www.eXcriba.org/ws/service/recordsmanagement/1.0",
    // "TreeFolder"));
    // org.apache.axis.description.ElementDesc elemField = new
    // org.apache.axis.description.ElementDesc();
    // elemField.setFieldName("id");
    // elemField.setXmlName(new
    // javax.xml.namespace.QName("http://www.eXcriba.org/ws/service/recordsmanagement/1.0",
    // "id"));
    // elemField.setXmlType(new
    // javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
    // elemField.setMinOccurs(0);
    // elemField.setNillable(false);
    // typeDesc.addFieldDesc(elemField);
    // elemField = new org.apache.axis.description.ElementDesc();
    // elemField.setFieldName("type");
    // elemField.setXmlName(new
    // javax.xml.namespace.QName("http://www.eXcriba.org/ws/service/recordsmanagement/1.0",
    // "type"));
    // elemField.setXmlType(new
    // javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
    // elemField.setMinOccurs(0);
    // elemField.setNillable(false);
    // typeDesc.addFieldDesc(elemField);
    // elemField = new org.apache.axis.description.ElementDesc();
    // elemField.setFieldName("contentsName");
    // elemField.setXmlName(new
    // javax.xml.namespace.QName("http://www.eXcriba.org/ws/service/recordsmanagement/1.0",
    // "contentsName"));
    // elemField.setXmlType(new
    // javax.xml.namespace.QName("http://www.alfresco.org/ws/model/content/1.0",
    // "Name"));
    // elemField.setMinOccurs(0);
    // elemField.setNillable(false);
    // elemField.setMaxOccursUnbounded(true);
    // typeDesc.addFieldDesc(elemField);
    // elemField = new org.apache.axis.description.ElementDesc();
    // elemField.setFieldName("entities");
    // elemField.setXmlName(new
    // javax.xml.namespace.QName("http://www.eXcriba.org/ws/service/recordsmanagement/1.0",
    // "entities"));
    // elemField.setXmlType(new
    // javax.xml.namespace.QName("http://www.eXcriba.org/ws/service/recordsmanagement/1.0",
    // "CCEntity"));
    // elemField.setMinOccurs(0);
    // elemField.setNillable(false);
    // elemField.setMaxOccursUnbounded(true);
    // typeDesc.addFieldDesc(elemField);
    // elemField = new org.apache.axis.description.ElementDesc();
    // elemField.setFieldName("folders");
    // elemField.setXmlName(new
    // javax.xml.namespace.QName("http://www.eXcriba.org/ws/service/recordsmanagement/1.0",
    // "folders"));
    // elemField.setXmlType(new
    // javax.xml.namespace.QName("http://www.eXcriba.org/ws/service/recordsmanagement/1.0",
    // "TreeFolder"));
    // elemField.setMinOccurs(0);
    // elemField.setNillable(false);
    // elemField.setMaxOccursUnbounded(true);
    // typeDesc.addFieldDesc(elemField);
    // }
    //
    // /**
    // * Return type metadata object
    // */
    // public static org.apache.axis.description.TypeDesc getTypeDesc() {
    // return typeDesc;
    // }
    //
    // /**
    // * Get Custom Serializer
    // */
    // public static org.apache.axis.encoding.Serializer getSerializer(
    // java.lang.String mechType,
    // java.lang.Class _javaType,
    // javax.xml.namespace.QName _xmlType) {
    // return
    // new org.apache.axis.encoding.ser.BeanSerializer(
    // _javaType, _xmlType, typeDesc);
    // }
    //
    // /**
    // * Get Custom Deserializer
    // */
    // public static org.apache.axis.encoding.Deserializer getDeserializer(
    // java.lang.String mechType,
    // java.lang.Class _javaType,
    // javax.xml.namespace.QName _xmlType) {
    // return
    // new org.apache.axis.encoding.ser.BeanDeserializer(
    // _javaType, _xmlType, typeDesc);
    // }

}
