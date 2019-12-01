package edu.hm.hafner.warningsngui.table;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.jenkins.plugins.datatables.api.DefaultAsyncTableContentProvider;
import io.jenkins.plugins.datatables.api.TableModel;

import java.util.List;

@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
public class ViewTable extends DefaultAsyncTableContentProvider {

    private final RepositoryStatistics repositoryStatistics;

    public ViewTable(RepositoryStatistics repositoryStatistics) {
        this.repositoryStatistics = repositoryStatistics;
    }

    @Override
    public TableModel getTableModel(String s) {
        return new TestTable(repositoryStatistics);
    }

    String getDisplayName() {
        return "DisplayName from ModelObject";
    }

    @Override
    public String getTableRows(String id) {
        //return super.getTableRows(id);
        return toJsonArray(getTableModel(id).getRows());
    }

    private String toJsonArray(List<Object> rows) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.setVisibility(PropertyAccessor.FIELD, JsonAutoDetect.Visibility.ANY);
            return (objectMapper).writeValueAsString(rows);
        } catch (JsonProcessingException var3) {
            throw new IllegalArgumentException(String.format("Can't convert table rows '%s' to JSON object", rows), var3);
        }
    }
}