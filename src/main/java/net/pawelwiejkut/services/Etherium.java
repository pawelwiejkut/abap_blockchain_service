package net.pawelwiejkut.services;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.web3j.crypto.Credentials;
import org.web3j.crypto.WalletUtils;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.methods.response.TransactionReceipt;
import org.web3j.protocol.http.HttpService;
import org.web3j.tx.Transfer;
import org.web3j.utils.Convert;

import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebService;
import javax.xml.ws.Endpoint;
import java.math.BigDecimal;


@WebService()
public class Etherium {

    private static final Logger log = LoggerFactory.getLogger(Etherium.class);
    private Credentials credentials;
    private Web3j web3j;

    public static void main(String[] args) throws Exception {
        Object implementor = new Etherium();
        String address = "http://localhost:18127/WsEth";
        Endpoint.publish(address, implementor);
    }

    @WebMethod
    public String login(@WebParam(name = "Password") String password, @WebParam(name = "token") String token,
                        @WebParam(name = "walletPath") String walletPath) throws Exception {

        // We start by creating a new web3j instance to connect to remote nodes on the network.
        // Note: if using web3j Android, use Web3jFactory.build(...
        web3j = Web3j.build(new HttpService(
                "https://rinkeby.infura.io/" + token));  // FIXME: Enter your Infura token here;
        log.info("Connected to Ethereum client version: "
                + web3j.web3ClientVersion().send().getWeb3ClientVersion());

        // We then need to load our Ethereum wallet file
        // FIXME: Generate a new wallet file using the web3j command line tools https://docs.web3j.io/command_line.html

        credentials = WalletUtils.loadCredentials(
                password,
                walletPath);

        return "Credentials loaded";
    }


    @WebMethod
    public String sendMoney(@WebParam(name = "ethAddress") String ethAddress) throws Exception {

        TransactionReceipt transferReceipt = Transfer.sendFunds(
                web3j, credentials,
                ethAddress,  // you can put any address here
                BigDecimal.ONE, Convert.Unit.WEI)  // 1 wei = 10^-18 Ether
                .send();
        return "Transaction complete, view it at https://rinkeby.etherscan.io/tx/"
                + transferReceipt.getTransactionHash();

    }




}
//        // Now lets deploy a smart contract
//        log.info("Deploying smart contract");
//        Greeter contract = Greeter.deploy(
//                web3j, credentials,
//                ManagedTransaction.GAS_PRICE, Contract.GAS_LIMIT,
//                "Hello blockchain world!").send();
//
//        String contractAddress = contract.getContractAddress();
//        log.info("Smart contract deployed to address " + contractAddress);
//        log.info("View contract at https://rinkeby.etherscan.io/address/" + contractAddress);
//
//        log.info("Value stored in remote smart contract: " + contract.greet().send());
//
//        // Lets modify the value in our smart contract
//        TransactionReceipt transactionReceipt = contract.newGreeting("Well hello again").send();
//
//        log.info("New value stored in remote smart contract: " + contract.greet().send());
//
//        // Events enable us to log specific events happening during the execution of our smart
//        // contract to the blockchain. Index events cannot be logged in their entirety.
//        // For Strings and arrays, the hash of values is provided, not the original value.
//        // For further information, refer to https://docs.web3j.io/filters.html#filters-and-events
//        for (Greeter.ModifiedEventResponse event : contract.getModifiedEvents(transactionReceipt)) {
//            log.info("Modify event fired, previous value: " + event.oldGreeting
//                    + ", new value: " + event.newGreeting);
//            log.info("Indexed event previous value: " + Numeric.toHexString(event.oldGreetingIdx)
//                    + ", new value: " + Numeric.toHexString(event.newGreetingIdx));
//        }
//    }
//}
