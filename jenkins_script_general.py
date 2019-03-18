# Liron Sfadia Bregman, PC/LR automation team
import jenkins, sys, xml.etree.ElementTree as ET, argparse
print(jenkins.__file__)
#sys.exit()

def PrintCommandLineOptions():
	args=parser.parse_args()

def main():
	try:
		parser=argparse.ArgumentParser(
	    description='''Jenkins Script Description''',
	    epilog="""---""")
		parser.add_argument('-args', dest='<host_machine_number> <test_name> <jenkins_slave_number>',action="store_true")
		parser.add_argument("host_vm_number", nargs='?', default="check_string_for_empty", help="the number of the Controller host machine")
		parser.add_argument("Jenkins_job_name", nargs='?', default="check_string_for_empty", help="the jenkins job name")
		parser.add_argument("test_name", nargs='?', default="check_string_for_empty", help="the test name")
		parser.add_argument("test_level", nargs='?', default="check_string_for_empty", help="the test level")
		parser.add_argument("jenkins_slave_number", nargs='?', default="check_string_for_empty", help="the number of the Jenkins Slave")
		args = parser.parse_args()

		if(len(sys.argv) < 5):
			raise ValueError('There are only {foo} arguments - You should enter 4 arguments!'.format(foo=len(sys.argv)-1))

		vm_name_val = 'myd-vm' + args.host_vm_number

		test_name_val = '/where:"test =~ /' + args.test_name +'/"'

		test_level_val = args.test_level

		jenkins_slave_val = 'myd-vm' + args.jenkins_slave_number + '.hpeswlab.net'

		filename = 'c:\\Python-Jenkins\\job_config.xml'

		jenkins_url = '<Jenkins_master_URL>'

		job_name = args.Jenkins_job_name
		
		server = jenkins.Jenkins(jenkins_url, username='<your_username>', password='<password>')
		
		version = server.get_version()

		user = server.get_whoami()

		print('Hello %s from Jenkins %s' % (user['fullName'], version))

		# Get job information dictionary.
		#print(server.get_job_info(job_name))
		#print(server.debug_job_info(job_name))

		if(server.get_job_name(job_name)):
			print(f'{job_name} exists')

		#print(server.get_whoami())

		open(filename, 'w').close()

		with open(filename,'a') as f:
			f.write(server.get_job_config(job_name))
			f.close()
		
		
		with open(filename, 'r') as file :
	  		filedata = file.read()
	  		file.close()

  
		# Write the file out again
		with open(filename, 'w') as file:
			file.write(filedata)
			file.close()

		param_dict = {'VM_NAME':vm_name_val,'TEST_FILTER':test_name_val,'TEST_LEVEL':test_level_val,'JENKINS_SLAVE':jenkins_slave_val}
		server.build_job(job_name, param_dict)

		#print("\n\n\n"+server.get_build_info(job_name,463))

		server = jenkins.Jenkins(jenkins_url)

		print(f"There are {server.jobs_count()} jobs in this server!")

	except jenkins.JenkinsException:
		print("JenkinsException:", sys.exc_info()[0])	
	except jenkins.NotFoundException:
		print("NotFoundException:", sys.exc_info()[0])	
	except jenkins.EmptyResponseException:
		print("EmptyResponseException:", sys.exc_info()[0])	
	except jenkins.BadHTTPException:
		print("BadHTTPException:", sys.exc_info()[0])	
	except jenkins.TimeoutException:
		print("TimeoutException:", sys.exc_info()[0])
	except ValueError as err:
		print(err)
		raise



if __name__ == '__main__':
  main()