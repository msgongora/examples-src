/*
 * Copyright (C) 2008-2009 _replaced_.
 *
 */
package org.eXcriba.repo.webservice;

import java.util.ArrayList;
import java.util.List;


/**
 * 
 * @author Marcel S. Gongora - <mrsanchez@uci.cu>
 */
public class Folder
{
    private String name;
    private String id;
    private String type;

    private List<Folder> folders;
    private List<String> contents;
    private List<CCEntity> entities;

    public Folder(String name)
    {
        this.name = name;
    }

    public Folder()
    {
    }


    public void addFolder(String folder) 
    {
        if (this.folders == null)
        {
            this.folders = new ArrayList<Folder>();
        }

        if (this.folders.contains(folder) == false)
        {
            this.folders.add(new Folder(folder));
        }
    }

    public void addContent(String content)
    {
        if (this.contents == null)
        {
            this.contents = new ArrayList<String>();
        }

        this.contents.add(new String(content));
    }

    public void addEntity(String entity)
    {
        if (this.entities == null)
        {
            this.entities = new ArrayList<CCEntity>();
        }

        this.entities.add(new CCEntity(entity));
    }

    /**
     * @return the name
     */
    public String getName()
    {
        return name;
    }

    /**
     * @param name
     *            the name to set
     */
    public void setName(String name)
    {
        this.name = name;
    }

    /**
     * @return the id
     */
    public String getId()
    {
        return id;
    }

    /**
     * @param id
     *            the id to set
     */
    public void setId(String id)
    {
        this.id = id;
    }

    /**
     * @return the type
     */
    public String getType()
    {
        return type;
    }

    /**
     * @param type
     *            the type to set
     */
    public void setType(String type)
    {
        this.type = type;
    }

    /**
     * @return the folders
     */
    public List<Folder> getChildren()
    {
        return folders;
    }

    /**
     * @return the contents
     */
    public List<String> getContents()
    {
        return contents;
    }

    /**
     * @return the entities
     */
    public List<CCEntity> getEntities()
    {
        return entities;
    }

 

}
