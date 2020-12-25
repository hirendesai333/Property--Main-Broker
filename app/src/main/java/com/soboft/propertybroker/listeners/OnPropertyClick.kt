package com.soboft.properybroker.listeners

import com.soboft.propertybroker.model.PropertyListModel

interface OnPropertyClick {

    fun onPropertyClick(currentItem: PropertyListModel)

}