/**
 *    Copyright 2016-2017 the original author or authors.
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */
package org.mybatis.dynamic.sql.delete.render;

import static org.mybatis.dynamic.sql.util.StringUtilities.spaceBefore;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

import org.mybatis.dynamic.sql.AbstractSqlSupport;
import org.mybatis.dynamic.sql.where.render.WhereSupport;

public class DeleteSupport extends AbstractSqlSupport {
    private Optional<String> whereClause;
    private Map<String, Object> parameters;
    
    private DeleteSupport(Builder builder) {
        super(builder.tableName);
        whereClause = Objects.requireNonNull(builder.whereClause);
        parameters = Objects.requireNonNull(builder.parameters);
    }
    
    public Map<String, Object> getParameters() {
        return parameters;
    }
    
    public String getFullDeleteStatement() {
        return "delete from" //$NON-NLS-1$
                + spaceBefore(tableName())
                + spaceBefore(whereClause);
    }

    public static class Builder {
        private String tableName;
        private Optional<String> whereClause = Optional.empty();
        private Map<String, Object> parameters = new HashMap<>();
        
        public Builder withTableName(String tableName) {
            this.tableName = tableName;
            return this;
        }
        
        public Builder withWhereSupport(Optional<WhereSupport> whereSupport) {
            whereClause = whereSupport.map(WhereSupport::getWhereClause);
            parameters.putAll(whereSupport.map(WhereSupport::getParameters).orElse(Collections.emptyMap()));
            return this;
        }
        
        public DeleteSupport build() {
            return new DeleteSupport(this);
        }
    }
}