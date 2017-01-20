package robotrace;

/**
* Materials that can be used for the robots.
*/
public enum Material {

    /** 
     * Gold material properties.
     * Modify the default values to make it look like gold.
     */
    GOLD (
        new float[]{0.24725f, 0.1995f, 0.0745f, 1.0f},
        new float[] {0.75164f, 0.60648f, 0.22648f, 1.0f},
        new float[] {0.628281f, 0.555802f, 0.366065f, 1.0f},
        new float[] {51.2f}

    ),

    /**
     * Silver material properties.
     * Modify the default values to make it look like silver.
     */
    SILVER (
        new float[] {0.19225f, 0.19225f, 0.19225f, 1.0f},
        new float[] {0.50754f, 0.50754f, 0.50754f, 1.0f},
        new float[] {0.508273f, 0.508273f, 0.508273f, 1.0f},
        new float[] {51.2f}

    ),

    /** 
     * Orange material properties.
     * Modify the default values to make it look like orange.
     */
    ORANGE (
        new float[] {0.0f, 0.0f, 0.0f, 1.0f},
        new float[] {0.992157f, 0.513726f, 0.0f, 1.0f},
        new float[] {0.0225f, 0.0225f, 0.0225f, 1.0f},
        new float[] {12.8f}

    ),

    /**
     * Wood material properties.
     * Modify the default values to make it look like Wood.
     */
    WOOD (
        new float[]{0.19f, 0.19f, 0.19f, 1.0f},
        new float[] {0.36f, 0.2f, 0.01f, 1.0f},
        new float[] {0.36f, 0.2f, 0.01f, 1.0f},
        new float[] {0.0f}

    ),
    
    
    /**
     * White material properties.
     * Modify the default values to make it look like White.
     */
    WHITE (
        new float[] {1.0f, 1.0f, 1.0f, 1.0f},
        new float[] {1.0f, 1.0f, 1.0f, 1.0f},
        new float[] {1.0f, 1.0f, 1.0f, 1.0f},
        new float[] {0.0f}
    );
    
    /** The ambient RGBA reflectance of the material. */
    float[] ambient;
    
    /** The diffuse RGBA reflectance of the material. */
    float[] diffuse;

    /** The specular RGBA reflectance of the material. */
    float[] specular;
    
    /** The specular exponent of the material. */
    float[] shininess;

    /**
     * Constructs a new material with diffuse and specular properties.
     */
    private Material(float[] ambient, float[] diffuse, float[] specular, float[] shininess) {
        this.ambient = ambient;
        this.diffuse = diffuse;
        this.specular = specular;
        this.shininess = shininess;
    }
}
