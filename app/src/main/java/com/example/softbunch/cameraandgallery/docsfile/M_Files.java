package com.example.softbunch.cameraandgallery.docsfile;

import android.text.TextUtils;

import com.example.softbunch.cameraandgallery.R;
import com.example.softbunch.cameraandgallery.utils.Utils;

import java.io.File;

/**
 * Created by softbunch on 11/29/16.
 */

public class M_Files {

    String filesId;
    String filesData;
    String filesMimeType;
    String filesSize;
    String filesDate;
    String filesTitle;


    public String getFilesId() {
        return filesId;
    }

    public void setFilesId(String filesId) {
        this.filesId = filesId;
    }

    public String getFilesData() {
        return filesData;
    }

    public void setFilesData(String filesData) {
        this.filesData = filesData;
    }

    public String getFilesMimeType() {
        return filesMimeType;
    }

    public void setFilesMimeType(String filesMimeType) {
        this.filesMimeType = filesMimeType;
    }

    public String getFilesSize() {
        return filesSize;
    }

    public void setFilesSize(String filesSize) {
        this.filesSize = filesSize;
    }

    public String getFilesDate() {
        return filesDate;
    }

    public void setFilesDate(String filesDate) {
        this.filesDate = filesDate;
    }

    public String getFilesTitle() {
        return filesTitle;
    }

    public void setFilesTitle(String filesTitle) {
        this.filesTitle = filesTitle;
    }

    public int getTypeDrawable() {
        if (getFileType() == Utils.FILE_TYPE.EXCEL) {
            return R.drawable.ic_txt;
        }

        if (getFileType() == Utils.FILE_TYPE.WORD) {
            return R.drawable.ic_doc;
        }

        if (getFileType() == Utils.FILE_TYPE.PPT) {
            return R.drawable.icon_ppt;
        }

        if (getFileType() == Utils.FILE_TYPE.PDF) {
            return R.drawable.ic_pdf;
        }

        if (getFileType() == Utils.FILE_TYPE.TXT) {
            return R.drawable.ic_txt;
        } else {
            return R.drawable.ic_txt;
        }
    }

    public Utils.FILE_TYPE getFileType() {
        String fileExtension = Utils.getFileExtension(new File(this.filesTitle));
        if (TextUtils.isEmpty(fileExtension))
            return Utils.FILE_TYPE.UNKNOWN;

        if (isExcelFile())
            return Utils.FILE_TYPE.EXCEL;
        if (isDocFile())
            return Utils.FILE_TYPE.WORD;
        if (isPPTFile())
            return Utils.FILE_TYPE.PPT;
        if (isPDFFile())
            return Utils.FILE_TYPE.PDF;
        if (isTxtFile())
            return Utils.FILE_TYPE.TXT;
        else
            return Utils.FILE_TYPE.UNKNOWN;
    }

    public boolean isExcelFile() {
        String[] types = {"xls", "xlsx"};
        return Utils.contains(types, this.filesTitle);
    }

    public boolean isDocFile() {
        String[] types = {"doc", "docx", "dot", "dotx"};
        return Utils.contains(types, this.filesTitle);
    }

    public boolean isPPTFile() {
        String[] types = {"ppt", "pptx"};
        return Utils.contains(types, this.filesTitle);
    }

    public boolean isPDFFile() {
        String[] types = {"pdf"};
        return Utils.contains(types, this.filesTitle);
    }

    public boolean isTxtFile() {
        String[] types = {"txt"};
        return Utils.contains(types, this.filesTitle);
    }


}
