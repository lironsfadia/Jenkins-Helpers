<STYLE>
BODY, TABLE, TD, TH, P {
  font-family:Verdana,Helvetica,sans serif;
  font-size:14px;
}

.bgcolor--failed {
    background-color: #ff454f;
}
.bgcolor--notperform {
    background-color: #ffff00;
}
.bgcolor--passed {
    background-color: #01a982;
}
</STYLE>
<BODY>

<%

import jenkins.model.*;
import hudson.model.*;
import hudson.util.*;
import hudson.tasks.*;
import hudson.plugins.git.*;
import hudson.scm.*
import jenkins.scm.*


def jobNames = ""

def testJob = Jenkins.instance.getAllItems(Job.class).find{it -> it.name == "x1"}
if(testJob==null)  throw new NoSuchElementException("No such job exists: x1");

def deployVerificationJob = Jenkins.instance.getAllItems(Job.class).find{it -> it.name == "x2"}
if(deployVerificationJob==null)  throw new NoSuchElementException("No such job exists: x2");

Jenkins.instance.getAllItems(Job.class).each{ 
 	jobNames += it.name + " - " + it.class
}
//throw new NoSuchElementException(jobNames)

//def deployJob = Jenkins.instance.getItem("x3")

def deployBuildRes = deployVerificationJob.lastBuild.result
def deployTime = new java.text.SimpleDateFormat("dd MMM yyyy", Locale.ENGLISH).format(deployVerificationJob.lastBuild.getTimestamp().getTime())
def todayTime = new java.text.SimpleDateFormat("dd MMM yyyy", Locale.ENGLISH).format(build.getTimestamp().getTime())
def testRes = testJob.lastBuild.result

def vmList = build.getBuildVariables().get("VM_NAME").split(',')
def vmsString = ""
for(vm in vmList)
{
	vmsString += vm + ".foo.net<br>"
}
def osNames = ["Windows 10 x64", "Windows 2012 R2"]

%>

<% if (deployTime.compareTo(todayTime) != 0){%>
	<table cellpadding="0" cellspacing="0">
	    <tr>
	    	<td>Nightly deploy on Product 12.63 QA Environments: </td>
	      	<td class="bgcolor--notperform">NOT PERFORMED</td>
	    </tr>
	    <tr>
	    	<td><br><strong>Reason:</strong> There is no new build available</td>
	      	<td></td>
	    </tr>
	</table>
<%} else if (deployBuildRes == Result.FAILURE) {%>
	<table cellpadding="0" cellspacing="0">
	    <tr>
	    	<td>Nightly deploy of Product on QA Environment: </td>
	      	<td class="bgcolor--failed">FAILED</td>
	    </tr>
	</table>
<%} else if (deployBuildRes == Result.SUCCESS && testRes != Result.SUCCESS){%>
	<table cellpadding="0" cellspacing="0">
	    <tr>
	    	<td>Nightly deploy of Product on QA Environment: </td>
	      	<td class="bgcolor--passed">PASSED</td>
	    </tr>
	    <tr>
	    	<td>Product Sanity: </td>
	      	<td class="bgcolor--failed">FAILED</td>
	    </tr>
	</table>
<%} else if (deployBuildRes == Result.SUCCESS && testRes == Result.SUCCESS){%>
	<table cellpadding="0" cellspacing="0">
	    <tr>
	    	<td>Nightly deploy of Product on QA Environment: </td>
	      	<td class="bgcolor--passed">PASSED</td>
	    </tr>
	    <tr>
	    	<td>Product Sanity: </td>
	      	<td class="bgcolor--passed">PASSED</td>
	    </tr>
	</table>
<%}%>

<p><br></p>
<table border="4" cellpadding="0" cellspacing="0">
	<col width="80">
  	<col width="150">
  	<col width="150">
	<%for (i = 0; i < vmList.length; i++) {%>
		<tr>
			<td>
				<div>Machine ${i}:</div>
			</td>
			<td>
				<div>${vmList[i]}</div>
			</td>
			<td>
				<div>${osNames[i]}</div>
			</td>
		</tr>
	<%}%>
</table>

<p><strong>Build URL:</strong> <A href="${rooturl}${build.url}">${rooturl}${build.url}</A></p>

</BODY>
