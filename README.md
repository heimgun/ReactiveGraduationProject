# Newton Algorand API
This is a Springboot API that connects to an Algorand node and indexer to provide various
data from the blockchain. The specific node and indexer we've been using are running on
the testnet, but the API is the same for any net and should work the same way.

To run the project you need to provide the following environmental variables:
 - ALGOD_URL
 - ALGOD_PORT
 - ALGOD_TOKEN
 - INDEXER_PORT

The indexer URL is assumed to be the same as ALGOD_URL, if this doesn't work for you, you need
to specify that in `src/resources/application.yml` (preferably as yet another environmental variable).

## Newton Bucks
The application will also have some integration specific to the asset Newton Bucks (NEWB) which is
created on the test net. The asset ID for this is specified through the properties file.

The specific integrations with Newton Bucks are TBD.