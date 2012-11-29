package com.wattshappening.db;

public class AggregateAppInfo extends DBEntry {

	//appID, historic cpu use, historic network use, hardware use, number of updates
	
	private int appUID;
	private long histCPU;
	private long histNet;
	private long histHard;
	private int numUpdates;
	
	public AggregateAppInfo(int appUID, long histCPU, long histNet, long histHard, int numUpdates) {
		this.appUID = appUID;
		this.histCPU = histCPU;
		this.histNet = histNet;
		this.histHard = histHard;
		this.numUpdates = numUpdates;
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
	public long getHistoricCPU() {
		return histCPU;
	}

	/**
	 * @return the histNet
	 */
	public long getHistoricNetwork() {
		return histNet;
	}

	/**
	 * @return the histHard
	 */
	public long getHistoricHardware() {
		return histHard;
	}

	/**
	 * @return the numUpdates
	 */
	public int getNumUpdates() {
		return numUpdates;
	}
	
	

}
