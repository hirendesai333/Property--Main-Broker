package com.illopen.agent.listeners

import com.illopen.agent.model.*


interface OnGoingClick{
    fun onGoingClick(currentItem : AssignedJobList)
}

interface OnCompletedJobClick {
    fun onCompletedJobsClick(currentItem: AllCompletedJobsList)
}

interface OnJobPropertyClick{
    fun onJobPropertyClick(currentItem : JobPropertyList)
}

interface OnCompletedJobPropertyClick{
    fun onCompletedJobPropertyClick(currentItem : JobPropertyList)
}