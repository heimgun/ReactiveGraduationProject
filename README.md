# Newton Algorand API
This is a Springboot API that connects to an Algorand node and indexer to provide various
data from the blockchain. The specific node and indexer we've been using are running on
the testnet, but the API is the same for any net and should work the same way.

To run the project you need to provide the following environmental variables:
 - `ALGOD_URL` (https://testnet.algoexplorerapi.io for AlgoExplorer)
 - `ALGOD_PORT` (443 for AlgoExplorer)
 - `INDEXER_URL` (https://testnet.algoexplorerapi.io/idx2 for AlgoExplorer)
 - `INDEXER_PORT` (443 for AlgoExplorer)

The indexer URL is assumed to be the same as ALGOD_URL, if this doesn't work for you, you need
to specify that in `src/resources/application.yml` (preferably as yet another environmental variable).

We're also assuming that you're using the AlgoExplorer API which doesn't require a token. If you're
running your own node you will need to change this in tha `application.yml` as well. This value should
most definitely be an environmental variable.