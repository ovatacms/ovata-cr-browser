/*
 * $Id: VirtualRootNode.java 2843 2019-11-22 09:31:33Z dani $
 * Created on 23.01.2019, 12:00:00
 * 
 * Copyright (c) 2019 by Ovata GmbH,
 * All rights reserved.
 * 
 * This software is the confidential and proprietary information
 * of Ovata GmbH ("Confidential Information").  You
 * shall not disclose such Confidential Information and shall use
 * it only in accordance with the terms of the license agreement
 * you entered into with Ovata GmbH.
 */
package ch.ovata.cr.browser.grid;

import ch.ovata.cr.api.Binary;
import ch.ovata.cr.api.CoreNodeTypes;
import ch.ovata.cr.api.Item;
import ch.ovata.cr.api.LockInfo;
import ch.ovata.cr.api.Node;
import ch.ovata.cr.api.NodeId;
import ch.ovata.cr.api.Property;
import ch.ovata.cr.api.Reference;
import ch.ovata.cr.api.Session;
import ch.ovata.cr.api.Value;
import ch.ovata.cr.api.security.Policy;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.ZonedDateTime;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.Map;
import java.util.Optional;
import javax.json.JsonObject;

/**
 *
 * @author dani
 */
public class VirtualRootNode implements Node {

    private final Session session;
    private final String rootPath;
    
    public VirtualRootNode( Session session, String rootPath) {
        this.session = session;
        this.rootPath = rootPath;
    }

    @Override
    public NodeId getNodeId() {
        return new NodeId( "VIRTUAL-ROOT-ID", 0);
    }

    @Override
    public String getId() {
        return "VIRTUAL-ROOT-ID";
    }

    @Override
    public long getRevision() {
        return 0l;
    }

    @Override
    public Date getLastModification() {
        return new Date();
    }

    @Override
    public Session getSession() {
        return this.session;
    }

    @Override
    public String getType() {
        return CoreNodeTypes.UNSTRUCTURED;
    }

    @Override
    public boolean isOfType(String type) {
        return CoreNodeTypes.UNSTRUCTURED.equals( type);
    }

    @Override
    public void setType(String type) {
        throw createNotSupportedException();
    }

    @Override
    public boolean isRoot() {
        return true;
    }

    @Override
    public boolean hasNodes() {
        return true;
    }

    @Override
    public boolean hasNode(String name) {
        return false;
    }

    @Override
    public Collection<Node> getNodes() {
        return Arrays.asList( this.session.getNodeByPath( this.rootPath));
    }

    @Override
    public Node getNode(String name) {
        throw createNotSupportedException();
    }

    @Override
    public Optional<Node> findNode(String name) {
        throw createNotSupportedException();
    }

    @Override
    public Node addNode(String name, String type) {
        throw createNotSupportedException();
    }

    @Override
    public Node getOrAddNode(String name, String type) {
        throw createNotSupportedException();
    }

    @Override
    public boolean isNew() {
        throw createNotSupportedException();
    }

    @Override
    public boolean isDirty() {
        throw createNotSupportedException();
    }

    @Override
    public Policy getPolicy() {
        throw createNotSupportedException();
    }

    @Override
    public boolean hasProperties() {
        throw createNotSupportedException();
    }

    @Override
    public boolean hasProperty(String name) {
        throw createNotSupportedException();
    }

    @Override
    public Optional<Property> findProperty(String name) {
        throw createNotSupportedException();
    }

    @Override
    public Property getProperty(String name) {
        throw createNotSupportedException();
    }

    @Override
    public Property setProperty(String name, Value value) {
        throw createNotSupportedException();
    }

    @Override
    public Collection<Property> getProperties() {
        return Collections.emptyList();
    }

    @Override
    public JsonObject getGeoJSON(String name) {
        throw createNotSupportedException();
    }

    @Override
    public Binary getBinary(String name) {
        throw createNotSupportedException();
    }

    @Override
    public Boolean getBoolean(String name) {
        throw createNotSupportedException();
    }

    @Override
    public LocalDate getDate(String name) {
        throw createNotSupportedException();
    }

    @Override
    public ZonedDateTime getTime(String name) {
        throw createNotSupportedException();
    }

    @Override
    public BigDecimal getDecimal(String name) {
        throw createNotSupportedException();
    }

    @Override
    public Double getDouble(String name) {
        throw createNotSupportedException();
    }

    @Override
    public Long getLong(String name) {
        throw createNotSupportedException();
    }

    @Override
    public String getString(String name) {
        throw createNotSupportedException();
    }

    @Override
    public Reference getReference(String name) {
        throw createNotSupportedException();
    }

    @Override
    public Value[] getArray(String name) {
        throw createNotSupportedException();
    }

    @Override
    public Map<String, Value> getMap(String name) {
        throw createNotSupportedException();
    }

    @Override
    public String getName() {
        return "Virtual Root Node";
    }

    @Override
    public String getPath() {
        return "../";
    }

    @Override
    public int getDepth() {
        return -1;
    }

    @Override
    public void rename(String name) {
        throw createNotSupportedException();
    }

    @Override
    public Node getParent() {
        return null;
    }

    @Override
    public void remove() {
        throw createNotSupportedException();
    }

    @Override
    public void accept(Item.Visitor visitor) {
        throw createNotSupportedException();
    }

    @Override
    public void moveTo(Node parent) {
        throw createNotSupportedException();
    }

    @Override
    public void clearProperties() {
        throw createNotSupportedException();
    }

    @Override
    public void lock() {
        throw createNotSupportedException();
    }

    @Override
    public void breakLock() {
        throw createNotSupportedException();
    }

    @Override
    public boolean isLocked() {
        throw createNotSupportedException();
    }

    @Override
    public Optional<LockInfo> getLockInfo() {
        throw createNotSupportedException();
    }
    
    private UnsupportedOperationException createNotSupportedException() {
        throw new UnsupportedOperationException("Not supported.");
    }
}
