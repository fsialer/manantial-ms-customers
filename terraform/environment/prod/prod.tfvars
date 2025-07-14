ENV                  = "prod"
PROJECT              = "streettalk"
AZ_GROUP_NAME        = "streettalk20250527gr"
AZ_LOCATION          = "East US2"
AZ_AKS_CLUSTER_NAME  = "streettalkaks"
ARM_SUBSCRIPTION     = ""
ARM_TENANT_ID        = ""
LISTS_SECRETS = [
  {
    "KEY" : "mongo-user",
    "VALUE" : "admin"
  },
  { "KEY" : "mongo-password",
    "VALUE" : "admin"
  },
  {
    "KEY" : "mongo-dbname-1",
    "VALUE" : "customers_db"
  }
]

