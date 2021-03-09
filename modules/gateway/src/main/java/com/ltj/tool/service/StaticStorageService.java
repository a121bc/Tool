package com.ltj.tool.service;

import com.ltj.tool.model.support.StaticFile;
import org.springframework.lang.NonNull;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * Static storage service interface class.
 *
 * @author Liu Tian Jun
 * @date 2021-03-08 11:59
 */
public interface StaticStorageService {

    String API_FOLDER_NAME = "api";

    /**
     * Static folder location.
     */
    String STATIC_FOLDER = "static";

    /**
     * Lists static folder.
     *
     * @return List<StaticFile>
     */
    List<StaticFile> listStaticFolder();

    /**
     * Delete file or folder by relative path
     *
     * @param relativePath relative path
     */
    void delete(@NonNull String relativePath);

    /**
     * Create folder.
     *
     * @param basePath   base path
     * @param folderName folder name must not be null
     */
    void createFolder(String basePath, @NonNull String folderName);

    /**
     * Update static file.
     *
     * @param basePath base path
     * @param file     file must not be null.
     */
    void upload(String basePath, @NonNull MultipartFile file);

    /**
     * Rename static file or folder.
     *
     * @param basePath base path must not be null
     * @param newName  new name must not be null
     */
    void rename(@NonNull String basePath, @NonNull String newName);

    /**
     * Save static file.
     *
     * @param path    path must not be null
     * @param content saved content
     */
    void save(@NonNull String path, String content);
}
