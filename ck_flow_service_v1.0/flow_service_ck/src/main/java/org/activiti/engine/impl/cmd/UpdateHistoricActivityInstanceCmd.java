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

package org.activiti.engine.impl.cmd;

import java.io.Serializable;

import org.activiti.engine.ActivitiIllegalArgumentException;
import org.activiti.engine.history.HistoricActivityInstance;
import org.activiti.engine.impl.interceptor.Command;
import org.activiti.engine.impl.interceptor.CommandContext;
import org.activiti.engine.impl.persistence.entity.HistoricActivityInstanceEntity;

/**
 * @author Tom Baeyens
 */
public class UpdateHistoricActivityInstanceCmd implements Command<Object>, Serializable {

  private static final long serialVersionUID = 1L;
  protected HistoricActivityInstance historicActivityInstance;

  public UpdateHistoricActivityInstanceCmd(HistoricActivityInstance historicActivityInstance) {
    this.historicActivityInstance = historicActivityInstance;
  }

  public Object execute(CommandContext commandContext) {

    if (historicActivityInstance == null) {
      throw new ActivitiIllegalArgumentException("historicActivityInstance is null");
    }
    commandContext
      .getHistoricActivityInstanceEntityManager()
      .updateHistoricActivityInstance((HistoricActivityInstanceEntity)historicActivityInstance);
    return null;
  }

}