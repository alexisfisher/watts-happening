package com.wattshappening.db;

public class AggregateAppInfo extends DBEntry {

	//appID, historic cpu use, historic network use, hardware use, number of updates
	
	private int appUID;
	private double histCPU;
	private double histNet;
	private double histHard;
	private int numUpdates;
	
	public AggregateAppInfo(int appUID, double d, double e, long histHard, int numUpdates) {
		this.appUID = appUID;
		this.histCPU = d;
		this.histNet = e;
		this.histHard = histHard;
		this.numUpdates = numUpdates;
	}

	public AggregateAppInfo() {
		// TODO Auto-generated constructor stub
	}

	/**
	 * @return the appUID
	 */
	public int getAppUID() {
		return appUID;
	}

	/**
	 * @return the histCPU
	 */
	public double getHistoricCPU() {
		return histCPU;
	}

	/**
	 * @return the histNet
	 */
	public double getHistoricNetwork() {
		return histNet;
	}

	/**
	 * @return the histHard
	 */
	public double getHistoricHardware() {
		return histHard;
	}

	/**
	 * @return the numUpdates
	 */
	public int getNumUpdates() {
		return numUpdates;
	}
	
	

}
