package writable;

import org.apache.hadoop.io.Writable;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

public class Flow implements Writable{

    private Integer upFlow;
    private Integer downFlow;
    private Integer sumFlow;

    public Flow() {
        super();
    }

    public Flow(int upFlow, int downFlow) {
        this.upFlow = upFlow;
        this.downFlow = downFlow;
        this.sumFlow=upFlow+downFlow;
    }

    public Integer getUpFlow() {
        return upFlow;
    }

    public void setUpFlow(int upFlow) {
        this.upFlow = upFlow;
    }

    public Integer getDownFlow() {
        return downFlow;
    }

    public void setDownFlow(int downFlow) {
        this.downFlow = downFlow;
    }

    public Integer getSumFlowm() {
        return upFlow+downFlow;
    }

    public void setSumFlow(int upFlow,int downFlow) {
        this.sumFlow = upFlow+downFlow;
    }

    public void setFlow(int upFlow,int downFlow){
        this.upFlow = upFlow;
        this.downFlow = downFlow;
        this.sumFlow=upFlow+downFlow;
    }

    @Override
    public void write(DataOutput dataOutput) throws IOException {
        dataOutput.writeInt(upFlow);
        dataOutput.writeInt(downFlow);
        dataOutput.writeInt(sumFlow);
    }

    @Override
    public void readFields(DataInput dataInput) throws IOException {
        this.upFlow=dataInput.readInt();
        this.downFlow=dataInput.readInt();
        this.sumFlow=dataInput.readInt();
    }

    @Override
    public String toString() {
        return upFlow + "\t" + downFlow + "\t" + sumFlow;
    }
}
