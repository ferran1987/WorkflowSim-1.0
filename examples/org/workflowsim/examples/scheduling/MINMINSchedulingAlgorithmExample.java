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
package org.workflowsim.examples.scheduling;

import java.io.File;
import java.util.Calendar;
import java.util.List;
import org.cloudbus.cloudsim.Log;
import org.cloudbus.cloudsim.core.CloudSim;
import org.workflowsim.CondorVM;
import org.workflowsim.WorkflowDatacenter;
import org.workflowsim.Job;
import org.workflowsim.WorkflowEngine;
import org.workflowsim.WorkflowPlanner;
import org.workflowsim.utils.ClusteringParameters;
import org.workflowsim.utils.OverheadParameters;
import org.workflowsim.utils.Parameters;
import org.workflowsim.utils.ReplicaCatalog;

/**
 * This MINMIN Scheduling Algorithm
 *
 * @author Weiwei Chen
 * @since WorkflowSim Toolkit 1.1
 * @date Nov 9, 2013
 */
public class MINMINSchedulingAlgorithmExample extends DataAwareSchedulingAlgorithmExample {

    ////////////////////////// STATIC METHODS ///////////////////////
    /**
     * Creates main() to run this example This example has only one datacenter
     * and one storage
     */
    public static void main(String[] args) {

        try {
            // First step: Initialize the WorkflowSim package. 
            /**
             * However, the exact number of vms may not necessarily be vmNum If
             * the data center or the host doesn't have sufficient resources the
             * exact vmNum would be smaller than that. Take care.
             */
            int vmNum = 5;//number of vms;
        	//System.out.println("MINMINSchAlg.j main: int vNum="+vmNum+", daxPath");
            /**
             * Should change this based on real physical path
             */
            String daxPath = "/home/ferran/Escriptori/EclipseFerran/WorkflowSim-1.0-Predict/config/dax/Montage_100.xml";
            //String daxPath = "/home/ferran/Escriptori/EclipseFerran/data_15_out.xml";
            //String daxPath = "/Users/weiweich/NetBeansProjects/WorkflowSim-1.0/config/dax/Montage_100.xml";

            File daxFile = new File(daxPath);
            if (!daxFile.exists()) {
                Log.printLine("Warning: Please replace daxPath with the physical path in your working environment!");
                return;
            }

            /**
             * Since we are using HEFT planning algorithm, the scheduling
             * algorithm should be static such that the scheduler would not
             * override the result of the planner
             */
        	//System.out.println("MINMINSchAl.j main: Parameters.SchedulingAlgorithm sch_method = Parameters.SchedulingAlgorithm.MINMIN");
            Parameters.SchedulingAlgorithm sch_method = Parameters.SchedulingAlgorithm.BIOBACKFILL;
        	//System.out.println("Nom Alg:"+sch_method.name()+". Classe:"+sch_method.getClass().getName());
        	Parameters.PlanningAlgorithm pln_method = Parameters.PlanningAlgorithm.INVALID;
            ReplicaCatalog.FileSystem file_system = ReplicaCatalog.FileSystem.LOCAL;

            /**
             * No overheads
             */
            OverheadParameters op = new OverheadParameters(0, null, null, null, null, 0);

            /**
             * No Clustering
             */
            ClusteringParameters.ClusteringMethod method = ClusteringParameters.ClusteringMethod.NONE;
            ClusteringParameters cp = new ClusteringParameters(0, 0, method, null);

            /**
             * Initialize static parameters
             */
        	System.out.println("MINMIN.j main: Parameters.init(vmNum="+vmNum+", daxPath, null,null, op, cp, sch_method, pln_method,null,0)");
            Parameters.init(vmNum, daxPath, null,
                    null, op, cp, sch_method, pln_method,
                    null, 0);

            ReplicaCatalog.init(file_system);

        	
            // before creating any entities.
            int num_user = 1;   // number of grid users
            Calendar calendar = Calendar.getInstance();
            boolean trace_flag = false;  // mean trace events

            // Initialize the CloudSim library
            CloudSim.init(num_user, calendar, trace_flag);
            // no poso WFSBasicEx1.createDatacenter("Datacenter_0") ja q WFSBasicEx1.j es besnet (extend) d MINMINSchAlg.j
        	System.out.println("MINMIN.j main: WfDatac datacenter0 = createDatac(Datacenter_0)");
            WorkflowDatacenter datacenter0 = createDatacenter("Datacenter_0"); // WorkflowDatacenter.ja dexistir, i la funcio WorkflowDatacenter createDatacenter tambe
            System.out.println("MINMIN.j main: datacenter0="+datacenter0);
            //System.out.println("MINMINSchAlg.j: main Nom centre dades:"+datacenter0.getName()+". Classe:"+datacenter0.getClass().getName());
            //System.out.println("ddd"+datacenter0);//org.wfs.WorkflowDatacenter@135fbaa4
            /**
             * Create a WorkflowPlanner with one schedulers.
             */
        	//System.out.println("MINMIN.j main: WfPla wfPlanner = new WfPlan(planner_0, 1) ");
            WorkflowPlanner wfPlanner = new WorkflowPlanner("planner_0", 1);
            //System.out.println("wfPlanner"+wfPlanner); //org.wfs.WorkflowPlanner@2b193f2d

            /**
             * Create a WorkflowEngine.
             */
            WorkflowEngine wfEngine = wfPlanner.getWorkflowEngine();
            /**
             * Create a list of VMs.The userId of a vm is basically the id of
             * the scheduler that controls this vm.
             */
            
            System.out.println("MINMIN.j main: List<CondorVM> vmlist0 = createVM(wfEngine.getSchedulerId(0), Parameters.getVmNum()="+Parameters.getVmNum()+");");

            //System.out.println("MINMINSchAlg.j: main AB crida HEFTPlaAlg.j createVM");
            List<CondorVM> vmlist0 = createVM(wfEngine.getSchedulerId(0), Parameters.getVmNum());
            
            System.out.println("MINMIN.j: vmlist0="+vmlist0);
            /**
             * Submits this list of vms to this WorkflowEngine.
             */
            wfEngine.submitVmList(vmlist0, 0);

            /**
             * Binds the data centers with the scheduler.
             */
            wfEngine.bindSchedulerDatacenter(datacenter0.getId(), 0);
        	System.out.println("MINMIN.j main: CloudSim.startSimulation");

            CloudSim.startSimulation();
        	System.out.println("MINMIN.j main: DP CloudSim.startSimulation");

            List<Job> outputList0 = wfEngine.getJobsReceivedList();
            CloudSim.stopSimulation();
            printJobList(outputList0);
        } catch (Exception e) {
            Log.printLine("The simulation has been terminated due to an unexpected error");
        }
    }
}
