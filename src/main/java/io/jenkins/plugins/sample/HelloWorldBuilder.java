package io.jenkins.plugins.sample;

import hudson.Extension;
import hudson.FilePath;
import hudson.Launcher;
import hudson.model.AbstractProject;
import hudson.model.Run;
import hudson.model.TaskListener;
import hudson.tasks.BuildStepDescriptor;
import hudson.tasks.Builder;
import hudson.util.FormValidation;
import jenkins.tasks.SimpleBuildStep;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.methods.StringRequestEntity;
import org.codehaus.jackson.map.ObjectMapper;
import org.jenkinsci.Symbol;
import org.kohsuke.stapler.DataBoundConstructor;
import org.kohsuke.stapler.DataBoundSetter;
import org.kohsuke.stapler.QueryParameter;

import javax.servlet.ServletException;
import java.io.IOException;
import java.util.HashMap;

public class HelloWorldBuilder extends Builder implements SimpleBuildStep {
    private final String name;
    private  String address;
    private  String envUuid;
    private  String nameSpace;
    private  String stack;
    private  String service;
    private  String serviceType;
    private  String image;
    private  String token;

    private boolean useFrench;

    private static final String  DISPLAY_NAME = "WiseCloud";

    private static final String  DEPLOY_URL = "%s/orchestration/api/env/%s/namespace/%s";

    @DataBoundConstructor
    public HelloWorldBuilder(String name, String address, String envUuid, String nameSpace,
                             String stack, String service, String serviceType, String image, String token) {
        this.name = name;
        this.address = address;
        this.envUuid = envUuid;
        this.nameSpace = nameSpace;
        this.stack = stack;
        this.service = service;
        this.serviceType = serviceType;
        this.image = image;
        this.token = token;
    }

    @DataBoundSetter
    public void setUseFrench(boolean useFrench) {
        this.useFrench = useFrench;
    }

    @Override
    public void perform(Run<?, ?> run, FilePath workspace, Launcher launcher, TaskListener listener) throws InterruptedException, IOException {
        run.addAction(new HelloWorldAction(name));
        listener.getLogger().println("the url :" + address + "stack: " + stack + "service:" + service + "type:" + serviceType + "image:" +
                image +
                "serviceType:" + serviceType);
        deployToWiseCloud(listener);
        if (useFrench) {
            listener.getLogger().println("Bonjour, " + name + "!");
        } else {
            listener.getLogger().println("Hello, " + name + "!");
        }
    }

    public void deployToWiseCloud(TaskListener listener) {
        try {
            String url = String.format(DEPLOY_URL, this.address, this.envUuid, this.nameSpace);
            HashMap<String, String> data = new HashMap<>();
            data.put("stackName", stack);
            data.put("serviceName", service);
            data.put("serviceType", serviceType);
            data.put("image", image);
            listener.getLogger().println("url:  " + url);
            listener.getLogger().printf("部署/升级镜像[%s]到应用堆栈%s->%s%n", image, stack, service);
            HttpClient httpClient = new HttpClient();
            PostMethod method = new PostMethod(url);
            String content = new ObjectMapper().writeValueAsString(data);
            listener.getLogger().println("json: " + content);
            method.setRequestEntity(new StringRequestEntity(content, "application/json", "UTF-8"));
            method.setRequestHeader("Authorization", token);
            int responseCode = httpClient.executeMethod(method);
            if (responseCode == HttpStatus.SC_OK) {
                listener.getLogger().println("更新镜像成功");
            }else {
                listener.getLogger().println("更新镜像失败，" + method.getResponseBodyAsString());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }


    }


    public String getName() {
        return name;
    }

    public boolean isUseFrench() {
        return useFrench;
    }


    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getEnvUuid() {
        return envUuid;
    }

    public void setEnvUuid(String envUuid) {
        this.envUuid = envUuid;
    }

    public String getNameSpace() {
        return nameSpace;
    }

    public void setNameSpace(String nameSpace) {
        this.nameSpace = nameSpace;
    }

    public String getStack() {
        return stack;
    }

    public void setStack(String stack) {
        this.stack = stack;
    }

    public String getService() {
        return service;
    }

    public void setService(String service) {
        this.service = service;
    }

    public String getServiceType() {
        return serviceType;
    }

    public void setServiceType(String serviceType) {
        this.serviceType = serviceType;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    @Symbol("greet")
    @Extension
    public static final class DescriptorImpl extends BuildStepDescriptor<Builder> {

        public FormValidation doCheckName(@QueryParameter String value,
                                          @QueryParameter String address,
                                          @QueryParameter String envUuid,
                                          @QueryParameter String nameSpace,
                                          @QueryParameter String stack,
                                          @QueryParameter String service,
                                          @QueryParameter String image,
                                          @QueryParameter String token)
                throws IOException, ServletException {
            if (value.length() == 0)
                return FormValidation.error("名称不能为空");
            if (address.length() == 0) {
                return FormValidation.error("地址不能为空");
            }
            if (envUuid.length() == 0) {
                return FormValidation.error("环境UUID不能为空");
            }
            if (nameSpace.length() == 0) {
                return FormValidation.error("命名空间不能为空");
            }
            if (stack.length() == 0) {
                return FormValidation.error("堆栈不能为空");
            }
            if (service.length() == 0) {
                return FormValidation.error("服务不能为空");
            }
            if (image.length() == 0) {
                return FormValidation.error("镜像不能为空");
            }
            if (token.length() == 0) {
                return FormValidation.error("TOKEN不能为空");
            }
            return FormValidation.ok();
        }

        @Override
        public boolean isApplicable(Class<? extends AbstractProject> aClass) {
            return true;
        }

        @Override
        public String getDisplayName() {
            return DISPLAY_NAME;
        }



    }

}
