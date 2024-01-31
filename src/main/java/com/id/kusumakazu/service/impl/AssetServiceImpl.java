package com.id.kusumakazu.service.impl;

import com.id.kusumakazu.domain.Asset;
import com.id.kusumakazu.domain.utility.ImageData;
import com.id.kusumakazu.domain.utility.LoadFile;
import com.id.kusumakazu.repository.AssetRepository;
import com.id.kusumakazu.service.AssetService;
import com.id.kusumakazu.service.dto.AssetDTO;
import com.id.kusumakazu.service.mapper.AssetMapper;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.mongodb.client.gridfs.model.GridFSFile;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.gridfs.GridFsOperations;
import org.springframework.data.mongodb.gridfs.GridFsTemplate;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

/**
 * Service Implementation for managing {@link Asset}.
 */
@Service
public class AssetServiceImpl implements AssetService {

    private final Logger log = LoggerFactory.getLogger(AssetServiceImpl.class);

    private final AssetRepository assetRepository;

    private final AssetMapper assetMapper;

    @Autowired
    private GridFsOperations operations;

    @Autowired
    private GridFsTemplate gridFsTemplate;

    public AssetServiceImpl(AssetRepository assetRepository, AssetMapper assetMapper) {
        this.assetRepository = assetRepository;
        this.assetMapper = assetMapper;
    }

    @Override
    public AssetDTO save(AssetDTO assetDTO) {
        log.debug("Request to save Asset : {}", assetDTO);
        Asset asset = assetMapper.toEntity(assetDTO);
        asset = assetRepository.save(asset);
        return assetMapper.toDto(asset);
    }

    @Override
    public AssetDTO update(AssetDTO assetDTO) {
        log.debug("Request to update Asset : {}", assetDTO);
        Asset asset = assetMapper.toEntity(assetDTO);
        asset = assetRepository.save(asset);
        return assetMapper.toDto(asset);
    }

    @Override
    public Optional<AssetDTO> partialUpdate(AssetDTO assetDTO) {
        log.debug("Request to partially update Asset : {}", assetDTO);

        return assetRepository
            .findById(assetDTO.getId())
            .map(existingAsset -> {
                assetMapper.partialUpdate(existingAsset, assetDTO);

                return existingAsset;
            })
            .map(assetRepository::save)
            .map(assetMapper::toDto);
    }

    @Override
    public List<AssetDTO> findAll() {
        log.debug("Request to get all Assets");
        return assetRepository.findAll().stream().map(assetMapper::toDto).collect(Collectors.toCollection(LinkedList::new));
    }

    @Override
    public Optional<AssetDTO> findOne(String id) {
        log.debug("Request to get Asset : {}", id);
        return assetRepository.findById(id).map(assetMapper::toDto);
    }

    @Override
    public void delete(String id) {
        log.debug("Request to delete Asset : {}", id);
        assetRepository.deleteById(id);
    }

    @Override
    public String gridFsUploadFile(MultipartFile file) {
        log.debug("testing gridFs upload insert file");
        try {
            String filename = file.getOriginalFilename();
            InputStream inputStream = file.getInputStream();
            gridFsTemplate.store(inputStream, filename, file.getContentType());
            inputStream.close();
            return "File uploaded successfully!";
        } catch (IOException e) {
            log.info(e.getMessage());
        }
        return "Failed ?";
    }

    @Override
    public String addFile(MultipartFile file) throws IOException {
        DBObject metadata = new BasicDBObject();

        metadata.put("fileSize", file.getSize());
        Object fileID = gridFsTemplate.store(file.getInputStream(), file.getOriginalFilename(), file.getContentType(), metadata);

        return fileID.toString();
    }

    @Override
    public LoadFile downloadFile(String id) throws IOException {
        log.info("Request to download one File by id: {}", id);
        GridFSFile gridFSFile = gridFsTemplate.findOne(new Query(Criteria.where("_id").is(id)));

        LoadFile loadFile = new LoadFile();

        if (gridFSFile != null && gridFSFile.getMetadata() != null) {
            loadFile.setFilename(gridFSFile.getFilename());
            loadFile.setFileType(gridFSFile.getMetadata().get("_contentType").toString());
            loadFile.setFileSize(gridFSFile.getMetadata().get("fileSize").toString());
            loadFile.setFile(IOUtils.toByteArray(operations.getResource(gridFSFile).getInputStream()));
        }

        return loadFile;
    }

    @Override
    public Optional<AssetDTO> findOneByAssetname(String assetName) {
        log.debug("Request to get one by  Asset : {}", assetName);
        return assetRepository.findOneByAssetName(assetName).map(assetMapper::toDto);
    }

    @Override
    public void saveAssetFromZIP(MultipartFile file) {
        log.debug("Request to save Asset (ZIP)");
        log.info("ZIP file NAME : {}", file.getOriginalFilename());

        List<ImageData> extractedImages = extractImagesFromZIPFile(file);
        for (ImageData data : extractedImages) {
            log.info("nama image yang ter ekstrak : {}", data.getImageName());
            AssetDTO dto = new AssetDTO();

            Optional<AssetDTO> assetsDTOOptional = findOneByAssetname(file.getOriginalFilename());
            if (assetsDTOOptional.isPresent()) {
                log.info("image already in db {} , updating.....", assetsDTOOptional.get().getAssetName());

                dto.setId(assetsDTOOptional.get().getId());
            }
            String extractedStockCode = EXTRACT_STOCK_CODE(data.getImageName());
            dto.setUuid(createRandomUUID("IMGE", random10Long()));
            dto.setAssetName(extractedStockCode);
            dto.setAssetType("ICON");
            dto.setAsset(data.getImageData());
            dto.setAssetContentType(String.valueOf(getImageMediaType(data.getImageName())));

            dto = save(dto);

            log.info("saving file {}", dto.getAssetName());
        }
    }

    private List<ImageData> extractImagesFromZIPFile(MultipartFile file) {
        log.info("process unpacking zip file via input stream");
        List<ImageData> extractedImages = new ArrayList<>();

        try (InputStream inputStream = file.getInputStream(); ZipInputStream zipInputStream = new ZipInputStream(inputStream)) {
            ZipEntry entry;
            while ((entry = zipInputStream.getNextEntry()) != null) {
                if (!entry.isDirectory() && isImageFile(entry.getName())) {
                    ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
                    byte[] buffer = new byte[1024];
                    int len;
                    while ((len = zipInputStream.read(buffer)) > 0) {
                        outputStream.write(buffer, 0, len);
                    }
                    extractedImages.add(new ImageData(entry.getName(), outputStream.toByteArray()));
                }
                zipInputStream.closeEntry();
            }
        } catch (IOException e) {
            log.error(e.getMessage());
        }

        return extractedImages;
    }

    private static MediaType getImageMediaType(String filePath) {
        String[] imageExtensions = { "jpg", "jpeg", "png", "gif", "bmp" };
        String extension = getFileExtension(filePath);

        if (extension != null) {
            for (String imageExt : imageExtensions) {
                if (extension.equalsIgnoreCase(imageExt)) {
                    return getMediaType(extension);
                }
            }
        }
        return null;
    }

    public static String EXTRACT_STOCK_CODE(String filePath) {
        int lastSlashIndex = filePath.lastIndexOf('/');
        int dotIndex = filePath.lastIndexOf('.');

        if (lastSlashIndex != -1 && dotIndex != -1 && lastSlashIndex < dotIndex) {
            return filePath.substring(lastSlashIndex + 1, dotIndex);
        } else {
            return null; // Or handle invalid format according to your needs
        }
    }

    private static String getFileExtension(String filePath) {
        int lastDotIndex = filePath.lastIndexOf('.');
        if (lastDotIndex != -1) {
            return filePath.substring(lastDotIndex + 1);
        }
        return null;
    }

    private static MediaType getMediaType(String extension) {
        switch (extension.toLowerCase()) {
            case "jpg":
            case "jpeg":
                return MediaType.IMAGE_JPEG;
            case "png":
                return MediaType.IMAGE_PNG;
            case "gif":
                return MediaType.IMAGE_GIF;
            case "bmp":
                return MediaType.valueOf("image/bmp"); // MediaType doesn't have a specific constant for BMP
            default:
                return null;
        }
    }

    private static String random10Long() {
        SecureRandom secureRandom = new SecureRandom();
        Long randomLong = secureRandom.nextLong();
        String random10Digit = Long.toString(Math.abs(randomLong)).substring(0, 10);
        return random10Digit;
    }

    private boolean isImageFile(String filename) {
        String[] imageExtensions = { ".jpg", ".jpeg", ".png", ".gif" }; // Add more extensions if needed
        for (String extension : imageExtensions) {
            if (filename.toLowerCase().endsWith(extension)) {
                return true;
            }
        }
        return false;
    }

    private String createRandomUUID(String staticId, String random10Digit) {
        log.debug("Request to createRandomUUID");

        String randomParameter2 = staticId;
        String randomHashedText = hashString(generateRandomText(22));
        String customUUID = random10Digit + randomParameter2 + randomHashedText.substring(0, 22);

        return customUUID;
    }

    private static String hashString(String input) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hashBytes = digest.digest(input.getBytes());
            StringBuilder hexString = new StringBuilder();

            for (byte hashByte : hashBytes) {
                String hex = Integer.toHexString(0xff & hashByte);
                if (hex.length() == 1) {
                    hexString.append('0');
                }
                hexString.append(hex);
            }
            return hexString.toString();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return "";
        }
    }

    private static String generateRandomText(Integer length) {
        SecureRandom secureRandom = new SecureRandom();
        String chars = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";
        StringBuilder randomText = new StringBuilder(length);
        for (int i = 0; i < length; i++) {
            randomText.append(chars.charAt(secureRandom.nextInt(chars.length())));
        }
        return randomText.toString();
    }
}
