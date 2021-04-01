package com.illopen.agent.listeners

import com.illopen.agent.model.*


interface OnGoingClick{
    fun onGoingClick(currentItem : AssignedJobList)
}

interface OnJobPropertyClick{
    fun onJobPropertyClick(currentItem : JobPropertyList)
}