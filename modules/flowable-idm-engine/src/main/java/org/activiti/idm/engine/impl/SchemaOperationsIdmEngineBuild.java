/* Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.activiti.idm.engine.impl;

import org.activiti.idm.engine.impl.db.DbSqlSession;
import org.activiti.idm.engine.impl.interceptor.Command;
import org.activiti.idm.engine.impl.interceptor.CommandContext;

/**
 * @author Tijs Rademakers
 * @author Joram Barrez
 */
public final class SchemaOperationsIdmEngineBuild implements Command<Object> {

  public Object execute(CommandContext commandContext) {
    DbSqlSession dbSqlSession = commandContext.getDbSqlSession();
    if (dbSqlSession != null) {
      dbSqlSession.performSchemaOperationsIdmEngineBuild();
    }
    return null;
  }
}