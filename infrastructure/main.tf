provider "azurerm" {
  features {}
}


resource "azurerm_resource_group" "rg" {
  name     = "${var.product}-${var.component}-${var.env}"
  location = var.location
  tags     = var.common_tags
}

data "azurerm_key_vault" "civil" {
  name                = "civil-${var.env}"
  resource_group_name = "civil-service-${var.env}"
}
