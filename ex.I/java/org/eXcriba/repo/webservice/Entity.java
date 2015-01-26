package org.eXcriba.repo.webservice;

import java.util.ArrayList;
import java.util.List;

public class Entity
{
    private String name;
    private List<Entity> entities;

    public Entity(String name)
    {
        this.name = name;
    }

    public Entity()
    {
    }


    /**
     * @param entity
     */
    public void addEntity(String entity)
    {
        if (this.entities == null)
        {
            this.entities = new ArrayList();
        }

        this.entities.add(new Entity(entity));
    }

 
    /**
     * @return the name
     */
    public String getName()
    {
        return name;
    }

    /**
     * @return the entities
     */
    public List<Entity> getChildren()
    {
        return entities;
    }
    
    public String[] getChildrenName()
    {
        int length = entities.size();
        String[] str = new String[length];
        for (int i = 0; i < length; i++)
        {
            str[i] = entities.get(i).getName();
        }
        return str;
    }
    
    /**
     * Determines whether this entity has any children. It is more
     * effecient to call this method rather than getChildren().size() as a
     * collection is not created if it is not required
     * 
     * @return true if it has children, false otherwise
     */
    public boolean hasChildren()
    {
        return (entities == null) ? false : true;
    }
    
    /**
     * Returns the number of children this entity has
     * 
     * @return The number of children
     */
    public int getChildCount()
    {
        return this.entities.size();
    }
  
}