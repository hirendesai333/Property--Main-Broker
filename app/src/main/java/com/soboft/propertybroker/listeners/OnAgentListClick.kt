package com.soboft.propertybroker.listeners

import com.soboft.propertybroker.model.BidListModel

interface OnAgentListClick {
    fun onRatingClicked(currentItem: BidListModel)
}