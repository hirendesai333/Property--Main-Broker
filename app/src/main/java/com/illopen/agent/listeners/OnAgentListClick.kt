package com.illopen.agent.listeners

import com.illopen.agent.model.BidListModel

interface OnAgentListClick {
    fun onRatingClicked(currentItem: BidListModel)
}