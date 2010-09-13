require 'buildr_bnd'
require 'buildr_iidea'
require 'buildr_ipojo'
require 'swung_weave_buildr'

VERSION_NUMBER = "1.0.0"
GROUP = "swinkar"

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
  project.version = VERSION_NUMBER
  project.group = GROUP
  ipr.template = _('src/etc/project-template.ipr')

  compile { swung_weave_enhance }

  package(:bundle).tap do |bnd|
    bnd['Export-Package'] = "swinkar.*;version=#{version}"
  end

  desc "Deploy files require to run to a Karaf instance"
  task :deploy_to_karaf do
    deployment_artifacts =
      [project('swinkar').package(:bundle), Buildr::SwungWeave.api_artifact]

    cp artifacts(deployment_artifacts).collect { |a| a.invoke; a.to_s }, "#{KARAF_DIR}/deploy"
  end

  desc "Deploy all files require to run to a Karaf instance"
  task :deploy_all_to_karaf => [:deploy_to_karaf] do
    cp_r Dir["#{_('src/main/dist')}/**"], KARAF_DIR
  end
end
