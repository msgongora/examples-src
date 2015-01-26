/*
 * Copyright (C) 2008-2009 _replaced_.
 *
 */
package org.eXcriba.config.xml;

import org.eXcriba.repo.webservice.CCEntity;
import org.eXcriba.repo.webservice.Content;
import org.eXcriba.repo.webservice.Folder;
import org.eXcriba.repo.webservice.recordsmanagement.Base;
import org.eXcriba.repo.webservice.recordsmanagement.TreeFolder;
//import org.eXcriba.repo.webservice.recordsmanagement.CCEntity;
//import org.eXcriba.repo.webservice.recordsmanagement.Folder;
import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.DomDriver;

/**
 * 
 * @author Marcel S. Gongora - <mrsanchez@uci.cu>
 */
public class XmlParser
{
    /**
     * Parsea el xml pasado por parametro como <code>String</code>.
     * 
     * @param xmlstream
     * @return
     * @throws Exception
     */

    public TreeFolder parsingCC(String xmlstream)
    {
        XStream xStream = new XStream(new DomDriver());

        xStream.alias("folder", TreeFolder.class);
        xStream.alias("entity", CCEntity.class);
        xStream.alias("content", Base.class);

        xStream.useAttributeFor(Base.class, "name");
        xStream.useAttributeFor(TreeFolder.class, "id");
        xStream.useAttributeFor(TreeFolder.class, "type");
//        xStream.useAttributeFor(String.class, "name");
        xStream.useAttributeFor(CCEntity.class, "id");

        return (TreeFolder) xStream.fromXML(xmlstream);
    }
    /**
     * 
     * @param xmlstream
     * @return
     * @throws Exception
     */
    // public Entity parsingEnt(String xmlstream) throws Exception
    // {
    // XStream xStream = new XStream(new DomDriver());
    //
    // xStream.alias("entity", Entity.class);
    // xStream.aliasField("entities", Entity.class, "entities");
    //
    // xStream.useAttributeFor(Entity.class, "name");
    //        
    //
    // return (Entity) xStream.fromXML(xmlstream);
    // }
    /**
     * Genera un cuadro de clasificación a partir del tipo de dato Folder.
     * 
     * @param ccObject
     * @return
     * @throws Exception
     */
    public String generateCC(Folder ccObject)
    {
        XStream xStream = new XStream();

        xStream.alias("folder", Folder.class);
        xStream.alias("entity", CCEntity.class);
        xStream.alias("content", Content.class);

        // xstream.addImplicitCollection(Folder.class, "entities");
        // xstream.addImplicitCollection(Folder.class, "contents");

        xStream.useAttributeFor(Folder.class, "name");
        xStream.useAttributeFor(Folder.class, "id");
        xStream.useAttributeFor(Folder.class, "type");

        xStream.useAttributeFor(Content.class, "name");

        xStream.useAttributeFor(CCEntity.class, "id");

        return xStream.toXML(ccObject);
    }

    
}
