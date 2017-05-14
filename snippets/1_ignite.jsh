import spark.*;
Service http = Service.ignite();
http.port(8888)
http.staticFiles.externalLocation("/home/kubam/workspaces/slides/java9-jshell/ui")
http.init()