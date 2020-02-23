package edu.hm.hafner.warningsngui.service.schedule.rest.response;

public class ToolsResponse {
    private Tool[] tools;

    public Tool[] getTools() {
        return tools;
    }

    public void setTools(Tool[] tools) {
        this.tools = tools;
    }

    public static class Tool {
        private String id;
        private String latestUrl;
        private String name;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getLatestUrl() {
            return latestUrl;
        }

        public void setLatestUrl(String latestUrl) {
            this.latestUrl = latestUrl;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }
    }
}
