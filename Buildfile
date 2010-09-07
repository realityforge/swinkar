require 'buildr_bnd'
require 'buildr_iidea'
require 'buildr_ipojo'
require 'swung_weave_buildr'

repositories.remote << 'https://repository.apache.org/content/repositories/releases'
repositories.remote << 'http://repository.springsource.com/maven/bundles/external'
repositories.remote << 'http://repository.code-house.org/content/repositories/release' # OSGi - jmx RI

repositories.remote << Buildr::Bnd.remote_repository
repositories.remote << Buildr::Ipojo.remote_repository

KARAF_DIR="../apache-karaf-2.0.1-SNAPSHOT/"

desc 'Swinkar: OSGi/Swing test framework'
define 'swinkar' do
  compile.with :osgi_core,
               :osgi_compendium, 
               Buildr::Ipojo.annotation_artifact,
               :ipojo_eventadmin,
               Buildr::SwungWeave.api_artifact
  project.ipojoize!
  project.version = '1.0'
  ipr.template = _('src/etc/project-template.ipr')

  compile { swung_weave_enhance }

  package(:bundle).tap do |bnd|
    bnd['Export-Package'] = "swinkar.*;version=#{version}"
  end

  desc "Deploy files require to run to a Karaf instance"
  task :deploy_to_karaf do
    cp artifacts([project('swinkar').package(:bundle)]).collect { |a| a.invoke; a.to_s }, "#{KARAF_DIR}/deploy/"
  end

  desc "Deploy all files require to run to a Karaf instance"
  task :deploy_all_to_karaf => [:deploy_to_karaf] do
    cp_r Dir["#{_('src/main/dist')}/**"], KARAF_DIR
  end
end
