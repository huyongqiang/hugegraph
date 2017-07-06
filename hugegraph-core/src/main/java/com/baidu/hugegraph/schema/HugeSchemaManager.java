/*
 * Copyright 2017 HugeGraph Authors
 *
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements. See the NOTICE file distributed with this
 * work for additional information regarding copyright ownership. The ASF
 * licenses this file to You under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */
package com.baidu.hugegraph.schema;

import java.util.ArrayList;
import java.util.List;

import com.baidu.hugegraph.backend.tx.SchemaTransaction;
import com.baidu.hugegraph.type.schema.EdgeLabel;
import com.baidu.hugegraph.type.schema.IndexLabel;
import com.baidu.hugegraph.type.schema.PropertyKey;
import com.baidu.hugegraph.type.schema.VertexLabel;
import com.google.common.base.Preconditions;

public class HugeSchemaManager implements SchemaManager {

    private final SchemaTransaction transaction;

    public HugeSchemaManager(SchemaTransaction transaction) {
        this.transaction = transaction;
    }

    @Override
    public PropertyKey makePropertyKey(String name) {
        PropertyKey propertyKey = new HugePropertyKey(name);
        propertyKey.transaction(this.transaction);
        return propertyKey;
    }

    @Override
    public VertexLabel makeVertexLabel(String name) {
        VertexLabel vertexLabel = new HugeVertexLabel(name);
        vertexLabel.transaction(this.transaction);
        return vertexLabel;
    }

    @Override
    public EdgeLabel makeEdgeLabel(String name) {
        EdgeLabel edgeLabel = new HugeEdgeLabel(name);
        edgeLabel.transaction(this.transaction);
        return edgeLabel;
    }

    @Override
    public IndexLabel makeIndexLabel(String name) {
        IndexLabel indexLabel = new HugeIndexLabel(name);
        indexLabel.transaction(this.transaction);
        return indexLabel;
    }

    @Override
    public PropertyKey propertyKey(String name) {
        PropertyKey propertyKey = this.transaction.getPropertyKey(name);
        Preconditions.checkArgument(propertyKey != null,
                "Undefined property key: '%s'", name);
        return propertyKey;
    }

    @Override
    public VertexLabel vertexLabel(String name) {
        VertexLabel vertexLabel = this.transaction.getVertexLabel(name);
        Preconditions.checkArgument(vertexLabel != null,
                "Undefined vertex label: '%s'", name);
        return vertexLabel;
    }

    @Override
    public EdgeLabel edgeLabel(String name) {
        EdgeLabel edgeLabel = this.transaction.getEdgeLabel(name);
        Preconditions.checkArgument(edgeLabel != null,
                "Undefined edge label: '%s'", name);
        return edgeLabel;
    }

    @Override
    public List<SchemaElement> desc() {
        List<SchemaElement> elements = new ArrayList<>();
        elements.addAll(this.transaction.getPropertyKeys());
        elements.addAll(this.transaction.getVertexLabels());
        elements.addAll(this.transaction.getEdgeLabels());
        elements.addAll(this.transaction.getIndexLabels());
        return elements;
    }

    @Override
    public SchemaElement create(SchemaElement element) {
        element.transaction(this.transaction);
        return element.create();
    }

    @Override
    public SchemaElement append(SchemaElement element) {
        element.transaction(this.transaction);
        return element.append();
    }
}
