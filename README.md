# ACINO Orchestrator GUI

The DISMI is a northbound interface that allows client applications to request to the Orchestrator an intent, i.e. connectivity between network end points with specific properties (bandwidth, latency, protection, etc.). The key concept behind DISMI is to let client applications specify *What* they need, not *How* to implement it in the network, targeting the features provided by the ACINO Orchestrator.

The DISMI client is a Java based Graphical User Interface (GUI) program that allows creating services and intents and submitting them to the Orchestrator [ACINO network orchestrator](https://github.com/ACINO-H2020/network-orchestrator).

![Alt text](/gui.png?raw=true " ")

## Run:
To execute use the following command:

`$ java -jar dismigui.jar http://<orchestrator-ip>:8181/onos/acino/api`

## Loading connection points:
The orchestrator provides the abstraction of physical connection points of the network. The connection points can be defined in a JSON file. The `connectionPts.json` is provided as an example.

To load the connection points,
click on `Connection Point -> Load From File`

It opens File Dialog Box and then select your configuration file (`<connection-points-file>.json`).

## Install a Service:
To install a new connectivity service, click on `Service - > New` and it will first ask for service name.

After specifying Service name, it opens a window where the users can provide various input for an intent.

Once you specified all necessary inputs, press `Save Intent` and then `Submit Service`. The intent is processed by the ACINO orchestrator and installed in the multi-layer network.
