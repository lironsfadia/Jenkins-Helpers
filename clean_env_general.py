# Liron Sfadia Bregman, PC/LR automation team
import sys, os, shutil, subprocess
from xml.dom import minidom

def main():
	username = "<>"
	password = "<>"
	try:
		dir = os.path.dirname(os.path.realpath(__file__))

		xmldoc = minidom.parse(f"{dir}\\<xml_file>.xml")

		itemlist = xmldoc.getElementsByTagName("name")

		print(len(itemlist))

		for item in itemlist:
			
			#if(item == "myd-vm08940"):
			#	continue
			
			server = f"{item.firstChild.nodeValue}.<domain_name>"
			remoteArtifactPath = f"\\\\{server}\\C$"
			subprocess.run(f"net use {remoteArtifactPath} /user:{username} {password}")
			for file in os.listdir(remoteArtifactPath):
				fileFullPath = f"{remoteArtifactPath}\{file}"
				if(os.path.isfile(fileFullPath) and (os.path.splitext(fileFullPath)[1] == '.lck') and os.access(fileFullPath, os.R_OK)):
					print(f"{fileFullPath} was removed successfully")
					os.remove(fileFullPath)
	except Exception as e:
		print(e)

if __name__ == '__main__':
	main()


