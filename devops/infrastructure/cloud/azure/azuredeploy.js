{
  "$schema": "http://schema.management.azure.com/schemas/2015-01-01/deploymentTemplate.json#",
  "contentVersion": "1.0.0.0",
  "parameters": {
    "newLabName": {
      "type": "string",
      "defaultValue": "TDADemoLab",
      "metadata": {
        "description": "The name of the new lab instance to be created."
      }
    },
    "timeZoneId": {
      "type": "string",
      "defaultValue": "UTC",
      "metadata": {
        "description": "The timezone of the lab."
      }
    },
    "labVmShutDownTime": {
      "type": "string",
      "minLength": 5,
      "maxLength": 5,
      "defaultValue": "21:00",
      "metadata": {
        "description": "Set 'Auto Shutdown' policy: The UTC time at which the Lab VMs will be automatically shutdown (E.g. 17:30, 20:00, 09:00)."
      }
    },
    "maxAllowedVmsPerUser": {
      "type": "int",
      "minValue": 0,
      "defaultValue": 20,
      "metadata": {
        "description": "Set 'max VM allowed per user' policy: The maximum number of VMs allowed per user."
      }
    },
    "maxAllowedVmsPerLab": {
      "type": "int",
      "minValue": 0,
      "defaultValue": 100,
      "metadata": {
        "description": "Set 'Total VMs allowed in Lab' policy: The maximum number of VMs allowed per lab."
      }
    },
    "allowedVmSizes": {
      "type": "string",
      "defaultValue": "\"Standard_A5\", \"Standard_A3\", \"Standard_A2\", \"Standard_A0\"",
      "minLength": 3,
      "metadata": {
        "description": "Set 'allowed VM sizes' policy: A comma-separated list of VM sizes that are allowed in the lab."
      }
    },
    "artifactRepoUri": {
      "type": "string",
      "defaultValue": "https://github.com/andresfuentes/Azure.git",
      "metadata": {
        "description": "Set 'artifact repository': the Git clone URI."
      }
    },
    "artifactRepoFolder": {
      "type": "string",
      "defaultValue": "/Artifacts",
      "metadata": {
        "description": "Set 'artifact repository': the target folder in the repo."
      }
    },
    "artifactRepoBranch": {
      "type": "string",
      "defaultValue": "master",
      "metadata": {
        "description": "Set 'artifact repository': the target branch in the repo."
      }
    },
    "artifactRepoDisplayName": {
      "type": "string",
      "defaultValue": "TDADemoLabPrivateRepo",
      "metadata": {
        "description": "Set 'artifact repository': the display name of the repo."
      }
    },
    "artifactRepoSecurityToken": {
      "type": "securestring",
      "defaultValue": "some-token-here",
      "metadata": {
        "description": "Set 'artifact repository': the personal access token of the repo."
      }
    },
    "artifactRepoType": {
      "type": "string",
      "defaultValue": "GitHub",
      "metadata": {
        "description": "Set 'artifact repository': the Git repo type."
      }
    },

    "VMSize": {
      "type": "string",
      "defaultValue": "Standard_A2",
      "metadata": {
        "description": "The size of all the new VMs to be created in the lab."
      }
    },

    "VMSize-desktop": {
      "type": "string",
      "defaultValue": "Standard_A3",
      "metadata": {
        "description": "The size of all the new VMs to be created in the lab."
      }
    },

    "username": {
      "type": "string",
      "defaultValue": "pwc",
      "metadata": {
        "description": "The username for the local account that will be created on all the new VMs."
      }
    },
    "password": {
      "type": "securestring",
      "defaultValue": "{pwc-password}",
      "metadata": {
        "description": "The password for the local account that will be created on all the new VMs."
      }
    },


    "masterBoxTemplateName": {
      "type": "string",
      "defaultValue": "TDA Dev Box",
      "metadata": {
        "description": "For the 1 out of 3 custom VM images: Name of the VM template being created or updated."
      }
    },
    "masterBoxTemplateDescription": {
      "type": "string",
      "defaultValue": "TDA Dev box environment",
      "metadata": {
        "description": "For the 1 out of 3 custom VM image: Details about the VM template being created or updated."
      }
    },

    "numberOfInstances": {
      "type": "int",
      "defaultValue": 5,
      "metadata": {
        "description": "Number of nodes in the clusterd."
      }
    },

    "numberOfDesktops": {
      "type": "int",
      "defaultValue": 2,
      "metadata": {
        "description": "Number of desktop in the clusterd."
      }
    },

    "masterBoxVMName": {
      "type": "string",
      "defaultValue": "master",
      "metadata": {
        "description": "For the 2nd VM created in the lab that uses the 1st custom VM image: The name of the new VM to be created."
      }
    },

    "serverPrefix": {
      "type": "string",
      "defaultValue": "TDADemoLab",
      "metadata": {
        "description": "Server name prefix"
      }
    }
  },
  "variables": {
    "newLabId": "[resourceId('Microsoft.DevTestLab/labs', parameters('newLabName'))]",
    "labSubnetName": "[concat(variables('labVirtualNetworkName'), 'Subnet')]",
    "labVirtualNetworkName": "[concat(parameters('newLabName'), 'Network')]",
    "labVirtualNetworkId": "[resourceId('Microsoft.DevTestLab/labs/virtualNetworks', parameters('newLabName'), variables('labVirtualNetworkName'))]",

    "networkSecurityGroupName": "[concat(parameters('newLabName'), 'SecurityGroup')]",

    "masterBoxTemplateResourceName": "[concat(parameters('newLabName'), '/', parameters('masterBoxTemplateName'))]",
    "masterBoxVmTemplateId": "[resourceId('Microsoft.DevTestLab/labs/customImages', parameters('newLabName'), parameters('masterBoxTemplateName'))]",


    "customScriptFilePath": "https://raw.githubusercontent.com/andresfuentes/Azure/master/Artifacts/Desktop/install-docker.sh",
    "customScriptCommandToExecute":"bash install-docker.sh",
    "xxcustomScriptCommandToExecute": "curl -fsSL https://experimental.docker.com/ | sudo sh && sudo usermod -aG docker pwc",

    "clusterServers" : ["swarm-master", "dtr", "builder", "database"]


  },
  "resources": [
    {
      "apiVersion": "2016-05-15",
      "type": "Microsoft.DevTestLab/labs",
      "name": "[trim(parameters('newLabName'))]",
      "location": "[resourceGroup().location]",
      "resources": [
      {
      "apiVersion": "2016-07-01",
      "type": "Microsoft.Network/networkSecurityGroups",
      "dependsOn": [
            "[variables('newLabId')]"
          ],
      "name": "[variables('networkSecurityGroupName')]",
      "location": "[resourceGroup().location]",
      "properties": {
        "securityRules": [
          {
            "name": "web-8080",
            "properties": {
              "description": "Web 8080",
              "protocol": "Tcp",
              "sourcePortRange": "8080",
              "destinationPortRange": "8080",
              "sourceAddressPrefix": "*",
              "destinationAddressPrefix": "*",
              "access": "Allow",
              "priority": 100,
              "direction": "Inbound"
            }
          },
          {
            "name": "ssh-22",
            "properties": {
              "description": "SSH Enabled",
              "protocol": "Tcp",
              "sourcePortRange": "22",
              "destinationPortRange": "22",
              "sourceAddressPrefix": "*",
              "destinationAddressPrefix": "*",
              "access": "Allow",
              "priority": 101,
              "direction": "Inbound"
            }
          },

          {
            "name": "swarm-2377",
            "properties": {
              "description": "Docker Swarm Manager",
              "protocol": "Tcp",
              "sourcePortRange": "2377",
              "destinationPortRange": "2377",
              "sourceAddressPrefix": "*",
              "destinationAddressPrefix": "*",
              "access": "Allow",
              "priority": 102,
              "direction": "Inbound"
            }
          },
          {
            "name": "registry-5000",
            "properties": {
              "description": "Docker Registry",
              "protocol": "Tcp",
              "sourcePortRange": "5000",
              "destinationPortRange": "5000",
              "sourceAddressPrefix": "*",
              "destinationAddressPrefix": "*",
              "access": "Allow",
              "priority": 103,
              "direction": "Inbound"
            }
          }
        ]
      }
    },
        {
          "apiVersion": "2016-05-15",
          "name": "[variables('labVirtualNetworkName')]",
          "type": "virtualNetworks",
          "dependsOn": [
            "[variables('newLabId')]",
            "[resourceId('Microsoft.Network/networkSecurityGroups/', variables('networkSecurityGroupName'))]"
          ]
        },
        {
          "apiVersion": "2016-05-15",
          "name": "LabVmsShutdown",
          "type": "schedules",
          "dependsOn": [
            "[variables('newLabId')]"
          ],
          "properties": {
            "status": "enabled",
            "timeZoneId": "[parameters('timeZoneId')]",
            "taskType": "LabVmsShutdownTask",
            "dailyRecurrence": {
                "time": "[replace(parameters('labVmShutDownTime'),':','')]"
            }
          }
        },
        {
          "apiVersion": "2016-05-15",
          "name": "default/MaxVmsAllowedPerUser",
          "type": "policySets/policies",
          "dependsOn": [
            "[variables('newLabId')]"
          ],
          "properties": {
            "description": "",
            "factName": "UserOwnedLabVmCount",
            "threshold": "[string(parameters('maxAllowedVmsPerUser'))]",
            "evaluatorType": "MaxValuePolicy",
            "status": "enabled"
          }
        },
        {
          "apiVersion": "2016-05-15",
          "name": "default/MaxVmsAllowedPerLab",
          "type": "policySets/policies",
          "dependsOn": [
            "[variables('newLabId')]"
          ],
          "properties": {
            "description": "",
            "factName": "LabVmCount",
            "threshold": "[string(parameters('maxAllowedVmsPerLab'))]",
            "evaluatorType": "MaxValuePolicy",
            "status": "enabled"
          }
        },
        {
          "apiVersion": "2016-05-15",
          "name": "default/AllowedVmSizesInLab",
          "type": "policySets/policies",
          "dependsOn": [
            "[variables('newLabId')]"
          ],
          "properties": {
            "description": "",
            "factName": "LabVmSize",
            "threshold": "[concat('[', trim(parameters('allowedVmSizes')), ']')]",
            "evaluatorType": "AllowedValuesPolicy",
            "status": "enabled"
          }
        },
        {
          "apiVersion": "2016-05-15",
          "name": "privaterepo885",
          "type": "artifactSources",
          "dependsOn": [
            "[variables('newLabId')]"
          ],
          "properties": {
            "uri": "[parameters('artifactRepoUri')]",
            "folderPath": "[parameters('artifactRepoFolder')]",
            "branchRef": "[parameters('artifactRepoBranch')]",
            "displayName": "[parameters('artifactRepoDisplayName')]",
            "securityToken": "[parameters('artifactRepoSecurityToken')]",
            "sourceType": "[parameters('artifactRepoType')]",
            "status": "Enabled"
          }
        }
      ]
    },
    {
            "type": "Microsoft.Network/virtualNetworks",
            "name": "[variables('labVirtualNetworkName')]",
            "apiVersion": "2016-03-30",
            "location": "[resourceGroup().location]",

            "properties": {
                "addressSpace": {
                    "addressPrefixes": [
                        "10.0.0.0/20"
                    ]
                },
                "dhcpOptions": {
                    "dnsServers": []
                },
                "subnets": [
                    {
                        "name": "[variables('labSubnetName')]",
                        "properties": {
                            "addressPrefix": "10.0.0.0/20",
                            "networkSecurityGroup": {
                                 "id": "[resourceId('Microsoft.Network/networkSecurityGroups/', variables('networkSecurityGroupName'))]"
                             }
                        }
                    }
                ]
            },
            "dependsOn": []
    },


    {
      "apiVersion": "2016-05-15",
      "type": "Microsoft.DevTestLab/labs/virtualMachines",
      "name": "[concat(parameters('newLabName'), '/', parameters('serverPrefix'),'-swarm-node', copyIndex())]",
      "location": "[resourceGroup().location]",
      "dependsOn": [
        "[variables('labVirtualNetworkId')]"
      ],
      "copy": {
          "count": "[parameters('numberOfInstances')]",
          "name": "virtualMachineLoop"
        },
      "properties": {
        "labVirtualNetworkId": "[variables('labVirtualNetworkId')]",
        "notes": "Ubuntu Server 16.04 LTS",
        "galleryImageReference": {
          "offer": "UbuntuServer",
          "publisher": "Canonical",
          "sku": "16.04.0-LTS",
          "osType": "Linux",
          "version": "latest"
        },
        "size": "[parameters('VMSize')]",
        "userName": "[parameters('userName')]",
        "password": "[parameters('password')]",
        "isAuthenticationWithSshKey": false,
        "labSubnetName": "[variables('labSubnetName')]",
        "disallowPublicIpAddress": false
      },
      "resources": [
        {
        "name": "[concat('install-docker')]",
        "apiVersion": "2016-05-15",
        "location": "[resourceGroup().location]",
        "dependsOn": [
          "[resourceId('Microsoft.DevTestLab/labs/virtualMachines', parameters('newLabName'), concat(  parameters('serverPrefix'),'-swarm-node', copyIndex()))]"
        ],

        "type": "extensions",
        "properties": {
          "autoUpgradeMinorVersion": true,
          "typeHandlerVersion": "1.4",
          "settings": {
              "fileUris": [
                "[variables('customScriptFilePath')]"
              ]
          },
          "protectedSettings": {
              "commandToExecute": "[concat(variables('customScriptCommandToExecute'))]"
          },
          "type": "CustomScriptForLinux",
          "publisher": "Microsoft.OSTCExtensions"
        }
      }
]
    },
      {
      "apiVersion": "2016-05-15",
      "type": "Microsoft.DevTestLab/labs/virtualMachines",
      "name": "[concat(parameters('newLabName'), '/', parameters('serverPrefix'),'-desktop', copyIndex())]",
      "location": "[resourceGroup().location]",
      "dependsOn": [
        "[variables('labVirtualNetworkId')]"
      ],
      "copy": {
          "count": "[parameters('numberOfDesktops')]",
          "name": "virtualMachineLoop"
        },
      "properties": {
        "labVirtualNetworkId": "[variables('labVirtualNetworkId')]",
        "notes": "Ubuntu Server 16.04 LTS",
        "galleryImageReference": {
          "offer": "UbuntuServer",
          "publisher": "Canonical",
          "sku": "16.04.0-LTS",
          "osType": "Linux",
          "version": "latest"
        },
        "size": "[parameters('VMSize-desktop')]",
        "userName": "[parameters('userName')]",
        "password": "[parameters('password')]",
        "isAuthenticationWithSshKey": false,
        "labSubnetName": "[variables('labSubnetName')]",
        "disallowPublicIpAddress": false
      },
      "resources": [
        {
        "name": "[concat('install-docker')]",
        "apiVersion": "2016-05-15",
        "location": "[resourceGroup().location]",
        "dependsOn": [
          "[resourceId('Microsoft.DevTestLab/labs/virtualMachines', parameters('newLabName'), concat(  parameters('serverPrefix'),'-desktop', copyIndex()))]"
        ],

        "type": "extensions",
        "properties": {
          "autoUpgradeMinorVersion": true,
          "typeHandlerVersion": "1.4",
          "settings": {
              "fileUris": [
                "[variables('customScriptFilePath')]"
              ]
          },
          "protectedSettings": {
              "commandToExecute": "[concat(variables('customScriptCommandToExecute'))]"
          },
          "type": "CustomScriptForLinux",
          "publisher": "Microsoft.OSTCExtensions"
        }
      }
]
    },
      {
      "apiVersion": "2016-05-15",
      "type": "Microsoft.DevTestLab/labs/virtualMachines",
      "name": "[concat(parameters('newLabName'), '/', parameters('serverPrefix'),'-',variables('clusterServers')[copyIndex()])]",
      "location": "[resourceGroup().location]",
      "dependsOn": [
        "[variables('labVirtualNetworkId')]"
      ],
      "copy": {
          "count": "[length(variables('clusterServers'))]",
          "name": "virtualMachineLoop"
        },
      "properties": {
        "labVirtualNetworkId": "[variables('labVirtualNetworkId')]",
        "notes": "Ubuntu Server 16.04 LTS",
        "galleryImageReference": {
          "offer": "UbuntuServer",
          "publisher": "Canonical",
          "sku": "16.04.0-LTS",
          "osType": "Linux",
          "version": "latest"
        },
        "size": "[parameters('VMSize')]",
        "userName": "[parameters('userName')]",
        "password": "[parameters('password')]",
        "isAuthenticationWithSshKey": false,
        "labSubnetName": "[variables('labSubnetName')]",
        "disallowPublicIpAddress": false
      },
      "resources": [
        {
        "name": "[concat('install-docker')]",
        "apiVersion": "2016-05-15",
        "location": "[resourceGroup().location]",
        "dependsOn": [
          "[resourceId('Microsoft.DevTestLab/labs/virtualMachines', parameters('newLabName'), concat(  parameters('serverPrefix'),'-',variables('clusterServers')[copyIndex()] ))]"
        ],

        "type": "extensions",
        "properties": {
          "autoUpgradeMinorVersion": true,
          "typeHandlerVersion": "1.4",
          "settings": {
              "fileUris": [
                "[variables('customScriptFilePath')]"
              ]
          },
          "protectedSettings": {
              "commandToExecute": "[concat(variables('customScriptCommandToExecute'))]"
          },
          "type": "CustomScriptForLinux",
          "publisher": "Microsoft.OSTCExtensions"
        }
      }


      ]
    }





  ],

  "outputs": {
    "labId": {
      "type": "string",
      "value": "[resourceId('Microsoft.DevTestLab/labs', parameters('newLabName'))]"
    },


    "masterBoxVmTemplateId": {
      "type": "string",
      "value": "[variables('masterBoxVmTemplateId')]"
    },

    "devVmId": {
      "type": "string",
      "value": "[resourceId('Microsoft.DevTestLab/labs/virtualMachines', parameters('newLabName'), parameters('masterBoxVMName'))]"
    }
  }
}
