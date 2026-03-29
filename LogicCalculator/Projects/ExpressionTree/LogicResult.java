package Projects.ExpressionTree;
import LinkedList.LinkedList;

public class LogicResult {
    public final String table;
    public final String classification;
    public final LinkedList<String> variables;
    public final boolean results[];

    public LogicResult(String table, String classification, LinkedList<String> variables, boolean results[]) {
        this.table = table;
        this.classification = classification;
        this.variables = variables;
        this.results = results;
    }

    public String getTable() {
        return table;
    }

    public String getClassification() {
        return classification;
    }

    public LinkedList<String> getVariables() {
        return variables;
    }

    @Override
    public String toString() {
        return this.table + "\nVEREDITO: " + this.classification;
    }
}