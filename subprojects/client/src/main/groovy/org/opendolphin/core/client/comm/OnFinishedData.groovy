package org.opendolphin.core.client.comm

/**
 * interface for data point handler
 */
interface OnFinishedData extends OnFinishedHandler {
    void onFinishedData(List<Map> data);
}