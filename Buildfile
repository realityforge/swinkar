gem 'buildr-bnd', :version => '0.0.5'
gem 'buildr-iidea', :version => '0.0.7'
gem 'buildr-ipojo', :version => '0.0.1'

require 'buildr_bnd'
require 'buildr_iidea'
require 'buildr_ipojo'

repositories.remote << 'https://repository.apache.org/content/repositories/releases'
repositories.remote << 'http://repository.springsource.com/maven/bundles/external'
repositories.remote << 'http://repository.code-house.org/content/repositories/release' # OSGi - jmx RI

repositories.remote << Buildr::Bnd.remote_repository
repositories.remote << Buildr::Ipojo.remote_repository

IPOJO_ANNOTATIONS = Buildr::Ipojo.annotation_artifact
EVENT_ADMIN = 'org.apache.felix:org.apache.felix.ipojo.handler.eventadmin:jar:1.6.0'

OSGI_CORE = 'org.apache.felix:org.osgi.core:jar:1.4.0'
OSGI_COMPENDIUM = 'org.apache.felix:org.osgi.compendium:jar:1.4.0'

KARAF_DIR="/home/peter/apache-karaf-2.0.1-SNAPSHOT/"

desc 'Swinkar: OSGi/Swing test framework'
define 'swinkar' do
  compile.with OSGI_CORE, IPOJO_ANNOTATIONS, OSGI_COMPENDIUM, EVENT_ADMIN
  project.ipojoize!
  project.version = '1.0'

  package(:bundle).tap do |bnd|
    bnd['Export-Package'] = "swinkar.*;version=#{version}"
  end

  desc "Deploy files require to run to a Karaf instance"
  task :deploy_to_karaf do
    cp artifacts([project('swinkar').package(:bundle)]).collect { |a| a.invoke; a.to_s }, "#{KARAF_DIR}/deploy/"
  end

  task :deploy_all_to_karaf => [:deploy_to_karaf] do
    cp_r Dir["#{_('src/main/dist')}/**"], KARAF_DIR
  end
end
