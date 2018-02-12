/*
 * Title:        CloudSim Toolkit
 * Description:  CloudSim (Cloud Simulation) Toolkit for Modeling and Simulation of Clouds
 * Licence:      GPL - http://www.gnu.org/copyleft/gpl.html
 *
 * Copyright (c) 2009-2012, The University of Melbourne, Australia
 */

package org.cloudbus.cloudsim;

import org.cloudbus.cloudsim.provisioners.PeProvisioner;

/**
 * CloudSim Pe (Processing Element) class represents CPU unit, defined in terms of Millions
 * Instructions Per Second (MIPS) rating.<br>
 * <b>ASSUMPTION:<b> All PEs under the same Machine have the same MIPS rating.
 * 
 * @author Manzur Murshed
 * @author Rajkumar Buyya
 * @since CloudSim Toolkit 1.0
 */
public class Pe {

	/** Denotes Pe is FREE for allocation. */
	public static final int FREE = 1;

	/** Denotes Pe is allocated and hence busy in processing Cloudlet. */
	public static final int BUSY = 2;

    public static final int VM_STATUS_BL = 3; //fb: blast 
    public static final int VM_STATUS_BM = 4; //fb: bwamem
    public static final int VM_STATUS_BO = 5; //fb: bowtie
    public static final int VM_STATUS_BW = 6; //fb: bwaaling
    public static final int VM_STATUS_HI = 7; //fb: hisat
    public static final int VM_STATUS_ST = 8; //fb: star
    public static final int VM_STATUS_SO = 9; //fb: soap
    public static final int VM_STATUS_PH = 10; //fb: phyml
    public static final int VM_STATUS_MR = 11; //fb: mrbayes
    public static final int VM_STATUS_FA = 12; //fb: fasttree
    public static final int VM_STATUS_RA = 13; //fb: raxml
	
	/**
	 * Denotes Pe is failed and hence it can't process any Cloudlet at this moment. This Pe is
	 * failed because it belongs to a machine which is also failed.
	 */
	public static final int FAILED = 3;

	/** The id. */
	private int id;

	// FOR SPACE SHARED RESOURCE: Jan 21
	/** The status of Pe: FREE, BUSY, FAILED: . */
	private int status;

	/** The pe provisioner. */
	private PeProvisioner peProvisioner;

	/**
	 * Allocates a new Pe object.
	 * 
	 * @param id the Pe ID
	 * @param peProvisioner the pe provisioner
	 * @pre id >= 0
	 * @pre peProvisioner != null
	 * @post $none
	 */
	public Pe(int id, PeProvisioner peProvisioner) {
		setId(id);
		setPeProvisioner(peProvisioner);

		// when created it should be set to FREE, i.e. available for use.
		status = FREE;
	}

	/**
	 * Sets the id.
	 * 
	 * @param id the new id
	 */
	protected void setId(int id) {
		this.id = id;
	}

	/**
	 * Gets the id.
	 * 
	 * @return the id
	 */
	public int getId() {
		return id;
	}

	/**
	 * Sets the MIPS Rating of this Pe.
	 * 
	 * @param d the mips
	 * @pre mips >= 0
	 * @post $none
	 */
	public void setMips(double d) {
		getPeProvisioner().setMips(d);
	}

	/**
	 * Gets the MIPS Rating of this Pe.
	 * 
	 * @return the MIPS Rating
	 * @pre $none
	 * @post $result >= 0
	 */
	public int getMips() {
		return (int) getPeProvisioner().getMips();
	}

	/**
	 * Gets the status of this Pe.
	 * 
	 * @return the status of this Pe
	 * @pre $none
	 * @post $none
	 */
	public int getStatus() {
		return status;
	}

	/**
	 * Sets Pe status to free, meaning it is available for processing. This should be used by SPACE
	 * shared hostList only.
	 * 
	 * @pre $none
	 * @post $none
	 */
	public void setStatusFree() {
		setStatus(FREE);
	}

	/**
	 * Sets Pe status to busy, meaning it is already executing Cloudlets. This should be used by
	 * SPACE shared hostList only.
	 * 
	 * @pre $none
	 * @post $none
	 */
	public void setStatusBusy() {
		setStatus(BUSY);
	}

	/**
	 * Sets this Pe to FAILED.
	 * 
	 * @pre $none
	 * @post $none
	 */
	public void setStatusFailed() {
		setStatus(FAILED);
	}

	/**
	 * Sets Pe status to either <tt>Pe.FREE</tt> or <tt>Pe.BUSY</tt>
	 * 
	 * @param status Pe status, <tt>true</tt> if it is FREE, <tt>false</tt> if BUSY.
	 * @pre $none
	 * @post $none
	 */
	public void setStatus(int status) {
		this.status = status;
	}

	/**
	 * Sets the pe provisioner.
	 * 
	 * @param peProvisioner the new pe provisioner
	 */
	protected void setPeProvisioner(PeProvisioner peProvisioner) {
		this.peProvisioner = peProvisioner;
	}

	/**
	 * Gets the Pe provisioner.
	 * 
	 * @return the Pe provisioner
	 */
	public PeProvisioner getPeProvisioner() {
		return peProvisioner;
	}

}
