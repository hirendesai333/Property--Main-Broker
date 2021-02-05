package com.soboft.propertybroker.listeners

import com.soboft.propertybroker.model.*

interface OnNewJobsClick {
    fun onNewJobsClick(currentItem: AvailableJobs)
}

interface OnGoingClick{
    fun onGoingClick(currentItem : AssignedJobList)
}

interface OnCompletedJobClick {
    fun onCompletedJobsClick(currentItem: AllCompletedJobsList)
}

interface OnJobPropertyClick{
    fun onJobPropertyClick(currentItem : JobPropertyList)
}

interface OnGoingJobPropertyClick{
    fun onGoingJobPropertyClick(currentItem : JobPropertyList)
}

interface OnCompletedJobPropertyClick{
    fun onCompletedJobPropertyClick(currentItem : JobPropertyList)
}