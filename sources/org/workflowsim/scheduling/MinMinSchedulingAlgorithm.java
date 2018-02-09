/**
 * Copyright 2012-2013 University Of Southern California
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */
package org.workflowsim.scheduling;

import java.util.ArrayList;
import java.util.List;
import org.cloudbus.cloudsim.Cloudlet;
import org.cloudbus.cloudsim.Vm;
import org.workflowsim.CondorVM;
import org.workflowsim.WorkflowSimTags;
import org.cloudbus.cloudsim.Pe; // fb
// cada maq 4 proc ex/wf/ex/WSBasicEx.j l200 . 5 vms ex/or/wf/ex/sc/MINMIN.j l56
/**
 * MinMin algorithm.
 *
 * @author Weiwei Chen
 * @since WorkflowSim Toolkit 1.0
 * @date Apr 9, 2013
 */
public class MinMinSchedulingAlgorithm extends BaseSchedulingAlgorithm {

    public MinMinSchedulingAlgorithm() {
        super();
    }
    private final List<Boolean> hasChecked = new ArrayList<>();

    @Override
    public void run() {						 						 									// getCloudletList=list ready jobs to sch en aq it
        int size = getCloudletList().size(); 			// es defineix a BaseSchEx.j i WfSchr.j			// Initially:size=16 ja q podem sch 16jobs (hi ha 16jobs amb depth=1; i 4*5ProcsIdle)        									 
        System.out.println("   MinMin.j: NOVA IT run(): size="+size+" getClList="+getCloudletList());   // [org.wfs.Job@..     
        hasChecked.clear();
        for (int t = 0; t < size; t++) { 
            hasChecked.add(false);
        }
        for (int i = 0; i < size; i++) { 																// Agafo 1r non-checked job de getCloudletList
            int minIndex = 0;
            Cloudlet minCloudlet = null; 							
            for (int j = 0; j < size; j++) { 															// Agafo el 1R JOB Q SIGUI NON CHECKED de getCloudletList
            	Cloudlet cloudlet = (Cloudlet) getCloudletList().get(j);// MinMin ext BaseSchAlg.j, on ia getCloudletList() (a Cloudlet.j no hi ha getCloudletList())
                if (!hasChecked.get(j)) {                          										//Entra if job j is not checked,aka:if hasn't beenScheduledYet 
                    minCloudlet = cloudlet;
                    minIndex = j; 
                    break; 																				// Va a if(minCloudlet==null)
                }
            }
            if (minCloudlet == null) { 																	// n entra mai
            	break;
            }            								   
            for (int j = 0; j < size; j++) {                             								// compara minCloudlet (d dalt) amb la resta,fins trobar1nou minCloudlet (nou min) si troba1nou minCloudlet,(no checkejat, si es checkejat el continue impedeix seguir) actualitza minCloudlet, segueix comparant (lactualitzat) minCloudlet amb tts els altres fins trobar1nou min
                Cloudlet cloudlet = (Cloudlet) getCloudletList().get(j);								// org.wfs.Job@..
                if (hasChecked.get(j)) { 						         								// i=0, hasChecked.get(j=0 & 1..)=F=> no entra. //i=1, hasChecked.get(j=0)=F; pero hasChecked.get(j=1)=T=>tornaAdalt(for,j=2).//XXSystem.out.println("j="+j+": Already Checked");
                    continue; 									        								// Torna dalt aq for j, impedint comparar amb algu ja checked.
                }										                 								// System.out.println("j="+j+": Comparo CL(j="+j+")="+cloudlet.getCloudletLength()+" vs minCL(min="+minIndex+")="+minCloudlet.getCloudletLength());
                long length = cloudlet.getCloudletLength(); 											// runtime*100
                if (length < minCloudlet.getCloudletLength()) {
                    minCloudlet = cloudlet;																// Actualitza minCloudlet i pos. minClodulet
                    minIndex = j;								               			     		   // System.out.println("     SI Act: minCL="+minCloudlet.getCloudletLength()+" minIndex="+minIndex);
                }                																		// if (length > minCloudlet.getCloudletLength()) {//System.out.println("     NO Act: minCL="+minCloudlet.getCloudletLength()+" minIndex="+minIndex);}
            } 																							// Ja hem repassat tt els non-checked jobs de "getCloudletList". Tenim minCloudlet (el +petit) i sa posicio            
            System.out.println("   MinMin.j: TROBAT. minCL(minIndex="+minIndex+")="+minCloudlet.getCloudletLength()+" vmSize="+getVmList().size()+" getVmList()="+getVmList());
            hasChecked.set(minIndex, true);  															// checkeja el minCloudlet
            int vmSize = getVmList().size(); 															// vmNum decl a MINMIN.j. nProcs decl a MINMIN.j->HEFTP.j createVM
            CondorVM firstIdleVm = null;                    											// (CondorVM)getVmList().get(0); declaro firstIdleVm, tipus CondorVM, val null

            ///////////////////////////////////////fb COMENSA "ABANS" ///////////////////////////////////////
            for (int nd = 0; nd < vmSize; nd++) { //fb
            	CondorVM vmaq = (CondorVM) getVmList().get(nd); 										//getVmList:[org.wfs.CoVM@,..]
            	System.out.println("   MinMin.j: vmaq num="+nd+". vmaq.getNumPes:"+vmaq.getNumberOfPes()+". vmaq.getPEsListToCondorVM().size() = "+vmaq.getPEsListToCondorVM().size());
            	System.out.println("   MinMin.j: vmaq.getPEsListToCondorVM = "+vmaq.getPEsListToCondorVM());
            	for (int pe=0; pe< vmaq.getNumberOfPes(); pe++){
            		System.out.println("    MinMin.j: Abans(EstatPE[v_maq="+nd+", pe="+vmaq.getPEsListToCondorVM().get(pe)+"])="+vmaq.getPEsListToCondorVM().get(pe).getStatus()); //vmaq.getPEsListToCondorVM().get(pe).setStatusFree(); vmaq.getPEsListToCondorVM().get(pe).setStatusBusy();            		            		
            	}            	
            	System.out.println("   MinMin.j: Abans(EstatMAQ[vmaq_n="+nd+"]="+vmaq.getState());          													              	
            }
            ///////////////////////////////////////fb ACABA "ABANS" ///////////////////////////////////////
            
            for (int j = 0; j < vmSize; j++) { 															// cerco la 1a maq idle
                CondorVM vm = (CondorVM) getVmList().get(j); 											// variable vm del tipus CondorVM
                if (vm.getState() == WorkflowSimTags.VM_STATUS_IDLE) {
                	// JUGAR AMB MAQUINA SEMI-IDLE I MIRANT QUINA MAQUINA HI HA OCUPADA
                    firstIdleVm = vm;
                    break; 																				// va a: if (firstIdleVm == null) {
                }
            }
            if (firstIdleVm == null) {
            	break;																					// cap maq idle. va a public void run() {
            }
            for (int j = 0; j < vmSize; j++) { 															// maq a maq d la llista d maqs
                CondorVM vm = (CondorVM) getVmList().get(j);
                if ((vm.getState() == WorkflowSimTags.VM_STATUS_IDLE)
                        && vm.getCurrentRequestedTotalMips() > firstIdleVm.getCurrentRequestedTotalMips()) {
                    firstIdleVm = vm; 																	// firstIdleVm = maq idle amb mes mips (em qedo amb la maq idle q tingui mes mips)
                }					  																	// mips es la pot de calcul. es fa servir x calc el temps dexecucio (no x mi sino q ho usa el simulador)
            }
            firstIdleVm.setState(WorkflowSimTags.VM_STATUS_BUSY); 										// firstIdleVm passa idle (1004) a busy (1003) (firstIdleVm.getState())
            minCloudlet.setVmId(firstIdleVm.getId());			  										// hi alloco minCloudlet
            getScheduledList().add(minCloudlet); 														// afegeixo el job "minCloudled" a la llista de jobs x schedular
            
            ///////////////////////////////////////fb COMENSA "DESPRES" ///////////////////////////////////////

            for (int j = 0; j < vmSize; j++) { //fb: estat maqs dp de schedular
            	CondorVM vmaq = (CondorVM) getVmList().get(j);
            	for (int pe = 0; pe < vmaq.getNumberOfPes(); pe++) {
            		System.out.println("    MinMin.j: Despres(EstatPE[v_maq="+j+", pe="+vmaq.getPEsListToCondorVM().get(pe)+"])="+vmaq.getPEsListToCondorVM().get(pe).getStatus()); //vmaq.getPEsListToCondorVM().get(pe).setStatusFree(); vmaq.getPEsListToCondorVM().get(pe).setStatusBusy();            		            		
            	}
            	System.out.println("   MinMin.j: Despres(EstatMAQ[vmaq_n="+j+"])="+vmaq.getState());          													              	
            }
            ///////////////////////////////////////fb ACABA "DESPRES" ///////////////////////////////////////
            
        }  																								 // un cop el job sta schedulat, seguim recorrent la llista (anem a for (int i = 0; i < size; i++) {)
    }
}
