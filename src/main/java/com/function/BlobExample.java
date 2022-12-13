package com.function;

import java.util.Optional;

import com.microsoft.azure.functions.ExecutionContext;
import com.microsoft.azure.functions.HttpMethod;
import com.microsoft.azure.functions.HttpRequestMessage;
import com.microsoft.azure.functions.HttpResponseMessage;
import com.microsoft.azure.functions.HttpStatus;
import com.microsoft.azure.functions.annotation.AuthorizationLevel;
import com.microsoft.azure.functions.annotation.BlobInput;
import com.microsoft.azure.functions.annotation.FunctionName;
import com.microsoft.azure.functions.annotation.HttpTrigger;
import com.microsoft.azure.functions.annotation.StorageAccount;

import com.microsoft.azure.functions.annotation.BlobInput;
import com.microsoft.azure.functions.annotation.QueueTrigger;

/**
 * Azure Function with HttpTrigger and BlobInput.
 * 
 * The following example shows a Java function that uses the HttpTrigger
 * annotation to receive a parameter containing the name of a file
 * in a blob storage container. The BlobInput annotation then reads the file
 * and passes its contents to the function as a byte[].
 *
 */

 /*
  * Provided that you've set up your local environment with the setupenvironment.ps1 script, you can try it out using http://localhost:7071/api/getBlobSizeHttp?file=testdata.txt 
  */

/**
 * Azure Functions with HTTP Trigger.
 */
public class HttpTriggerBlobInput {

  @FunctionName("getBlobSizeHttp")
  @StorageAccount("Storage_Account_Connection_String")
  public HttpResponseMessage blobSize(
    @HttpTrigger(name = "req", 
      methods = {HttpMethod.GET}, 
      authLevel = AuthorizationLevel.ANONYMOUS) 
    HttpRequestMessage<Optional<String>> request,
    @BlobInput(
      name = "file", 
      dataType = "binary", 
      path = "samples-workitems/{Query.file}") 
    byte[] content,
    final ExecutionContext context) {

      //extract information about the requested blob
      String filename = request.getQueryParameters().get("file");
      Integer fileLength = content.length;

      //Construct a body for the response
      String body = "The size of \"" + filename + "\" is: " + fileLength + " bytes";
      
      // build HTTP response
      return request.createResponseBuilder(HttpStatus.OK)
        .body(body)
        .build();
  }
}

/**
 * Azure Function with QueueTrigger and BlobInput.
 * 
 * The following example shows a Java function that uses the QueueTrigger
 * annotation to receive a message containing the name of a file
 * in a blob storage container. The BlobInput annotation then reads the file
 * and passes its contents to the function as a byte[].
 *
 */


 /*
  * Provided that you've set up your local environment with the setupenvironment.ps1 script, 
  * you can try it out by adding a message to the myqueue-items-sample queue with the payload "testdata.txt"
  * Tip: Use the Azure Storage Explorer to add messages to your queue
  */

public class QueueTriggerBlobInput {

  @FunctionName("getBlobSize")
  @StorageAccount("Storage_Account_Connection_String")
  public void blobSize(
    @QueueTrigger(
      name = "filename", 
      queueName = "myqueue-items-sample") 
    String filename,
    @BlobInput(
      name = "file", 
      dataType = "binary", 
      path = "samples-workitems/{queueTrigger}") 
    byte[] content,
    final ExecutionContext context) {
      context.getLogger().info("The size of \"" + filename + "\" is: " + content.length + " bytes");
  }
}