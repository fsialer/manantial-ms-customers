resource "azurerm_resource_group" "my_group" {
  name = var.name
  location = var.location
  tags = {
    environment=var.env
    project=var.project
  }
}