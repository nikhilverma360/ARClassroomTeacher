package com.nikhilverma360.arclassroomteacher;

import androidx.appcompat.app.AppCompatActivity;

import android.net.Uri;
import android.os.Bundle;
import android.widget.Toast;

import com.google.ar.sceneform.AnchorNode;
import com.google.ar.sceneform.rendering.ModelRenderable;
import com.google.ar.sceneform.ux.ArFragment;
import com.google.ar.sceneform.assets.RenderableSource;
import com.google.ar.sceneform.ux.TransformableNode;

import java.io.File;
public class ArSceneform extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ar_sceneform);

        ArFragment arFragment = (ArFragment) getSupportFragmentManager().findFragmentById(R.id.ux_fragment);


        findViewById(R.id.downloadbtn)
                .setOnClickListener(v -> {
                    try {
                        File file = new File("https://echoar-storage.s3-accelerate.amazonaws.com/withered-shape-6701/2a32a463-7db5-43d9-950b-a7d70f2dedb6.glb");
                        buildModel(file);
                    } catch (Exception e) {
                        e.printStackTrace();
                        Toast.makeText(this, "code fatt gya", Toast.LENGTH_SHORT).show();
                    }



                });

        arFragment.setOnTapArPlaneListener((hitResult, plane, motionEvent) -> {

            AnchorNode anchorNode = new AnchorNode(hitResult.createAnchor());
            anchorNode.setParent(arFragment.getArSceneView().getScene());
            TransformableNode transformableNode = new TransformableNode(arFragment.getTransformationSystem());
            transformableNode.getScaleController().canStartTransformation(null);
            transformableNode.getScaleController().setMinScale(0.2f);
            transformableNode.setParent(anchorNode);
            transformableNode.setRenderable(renderable);
            //arFragment.getArSceneView().getScene().addChild(anchorNode);
            transformableNode.select();

        });

    }

    private ModelRenderable renderable;

    private void buildModel(File file) {

        RenderableSource renderableSource = RenderableSource
                .builder()
                .setSource(this, Uri.parse(file.getPath()), RenderableSource.SourceType.GLB)
                .setRecenterMode(RenderableSource.RecenterMode.ROOT)
                .build();

        ModelRenderable
                .builder()
                .setSource(this, renderableSource)
                .setRegistryId(file.getPath())
                .build()
                .thenAccept(modelRenderable -> {
                    Toast.makeText(this, "Model built", Toast.LENGTH_SHORT).show();;
                    renderable = modelRenderable;
                });

    }

}