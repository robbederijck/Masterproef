
import java.nio.FloatBuffer;
import java.util.Map;

import org.tensorflow.*;
import org.tensorflow.Operation;
import org.tensorflow.ndarray.FloatNdArray;
import org.tensorflow.ndarray.buffer.DataBuffer;
import org.tensorflow.ndarray.buffer.DataBuffers;
import org.tensorflow.ndarray.buffer.FloatDataBuffer;
import org.tensorflow.proto.framework.NodeDef;
import org.tensorflow.proto.framework.SignatureDef;
import org.tensorflow.ndarray.NdArrays;
import org.tensorflow.ndarray.Shape;
import org.tensorflow.types.TFloat32;


import java.util.*;


public class MachineLearningModel {

    SavedModelBundle thisModel = null;
    Session thisSession = null;
    Tensor inputTensor = null;



    public MachineLearningModel() throws Exception {

        thisModel = SavedModelBundle.load("src/main/resources/SavedModel", "serve");
        thisSession = thisModel.session();
        inputTensor = TFloat32.tensorOf(Shape.of(1, 322, 7));
        System.out.println("Model loaded with Tensorflow version: " + TensorFlow.version());

    }

    public double predict(float[][][] dataNMBS){

        /*
        FloatNdArray matrix3d = NdArrays.ofFloats(Shape.of(1, 322, 7));


        matrix3d.elements(0).forEach( matrix -> {
                    for(int i = 0; i < 322; i++){
                        matrix.set(NdArrays.vectorOf(dataNMBS[0][i][0], dataNMBS[0][i][1], dataNMBS[0][i][2], dataNMBS[0][i][3], dataNMBS[0][i][4], dataNMBS[0][i][5], dataNMBS[0][i][6]), i);
                    }
                }
        );

        /*

        long[] shape = new long[] {1, 322, 7};
        FloatBuffer buf = FloatBuffer.allocate(322 * 7);
        for (int i = 0; i < 322; ++i) {
            for(int j = 0; j < 7; j++){
                buf.put(dataNMBS[0][i][j]);
            }
        }
        buf.flip();

         */

        int index = 0;
        float[] newData = new float[322 * 7];
        for (int i = 0; i < 322; ++i) {
            for(int j = 0; j < 7; j++){
                newData[index] = dataNMBS[0][i][j];
                index++;
            }
        }

        FloatDataBuffer buffer = DataBuffers.of(newData);
        long[] shape = new long[] {1, 322, 7};
        Shape thisShape = Shape.of(shape);

        inputTensor = TFloat32.tensorOf(thisShape, buffer);


        //Tensor inputTensor = NdArrays.vectorOf(matrix3d);

        /*
        long[] shape = new long[] {1, 322, 7};
        FloatBuffer buf = FloatBuffer.allocate(322 * 7);
        for (int i = 0; i < 322; ++i) {
            for(int j = 0; j < 7; j++){
                buf.put(dataNMBS[0][i][j]);
            }
        }
        buf.flip();

        /*
        Tensor<Float> inputTensor = Tensor.create(shape, buf);
        thisModel.metaGraphDef();


        try(Session s = thisModel.session()){
            Tensor result = s.runner().feed("input", inputTensor).run().get(0);
            System.out.println(result);
        }

         */

        /*

        Map<String, Tensor> feed_dict = new HashMap<>();
        feed_dict.put("inp1", inputTensor);

        Map<String, Tensor> result = thisModel.function("serving_default").call(feed_dict);

         */


        List<Tensor> results = thisSession.runner().feed("serving_default_inp1", inputTensor).fetch("StatefulPartitionedCall").run();

        Tensor resultTensor = results.get(0);

        float[][] resultMatrix = new float[322][6];
        float[] bufferRead = new float[322 * 6];
        float[][] oneHotEncodedVector = new float[322][6];

        resultTensor.asRawTensor().data().asFloats().read(bufferRead);

        index = 0;
        for(int i = 0; i < 322; i++){
            float max = 0.0f;
            int maxIndex = -1;
            for(int j = 0; j < 6; j++){
                float value = bufferRead[index];
                resultMatrix[i][j] = value;

                if(value > max){
                    max = value;
                    maxIndex = j;
                }
                index++;
            }

            for(int j = 0; j < 6; j++){
                if(j == maxIndex){
                    oneHotEncodedVector[i][j] = 1.0f;
                }else{
                    oneHotEncodedVector[i][j] = 0.0f;
                }
            }
        }

        int total = 0;
        int som = 0;
        for(int i = 0; i < 322; i++){
            Boolean skipped = false;
            for(int j = 0; j < 6; j++){
                if(dataNMBS[0][i][j] == -1.0f){
                    skipped = true;
                    break;
                }else{
                    som += (int) (dataNMBS[0][i][j] * oneHotEncodedVector[i][j]);
                }
            }
            if(!skipped) total += 1;
        }

        double accuracy = (som / (double) total);
        return accuracy;

    }

}
