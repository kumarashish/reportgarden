package model;

public class DependencyModel implements Comparable  {
    String Dependency;
    int COUNT;

    public DependencyModel(String dependency, int count)
    {
        this.Dependency=dependency;
        this.COUNT=count;

    }

    public String getDependency() {
        return Dependency;
    }

    public int
    getCount() {
        return COUNT;
    }

    public void setDependency(String dependency) {
        this.Dependency = dependency;
    }

    public void setCount(int count) {
        this.COUNT = count;
    }

    @Override
    public int compareTo(Object model) {
        int count=((DependencyModel)model).getCount();
        /* For Descending order*/
        return count-this.COUNT;
    }
}
