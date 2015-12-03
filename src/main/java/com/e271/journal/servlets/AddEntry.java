/*
 * The MIT License
 *
 * Copyright 2015 Eugenio Ochoa.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package com.e271.journal.servlets;

import com.e271.journal.core.Journal;
import com.e271.journal.core.User;
import com.e271.journal.core.exceptions.TooManyCharacters;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Arrays;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.apache.tomcat.util.http.fileupload.FileItem;
import org.apache.tomcat.util.http.fileupload.FileUploadException;
import org.apache.tomcat.util.http.fileupload.disk.DiskFileItemFactory;
import org.apache.tomcat.util.http.fileupload.servlet.ServletFileUpload;

/**
 *
 * @author Eugenio Ochoa
 */
public class AddEntry extends HttpServlet {

    private static final Set<String> FORM_FIELDS = new HashSet<>(Arrays.asList(
            new String[] {"title","content","date","photo"}
    ));
    /**
     * Handles the HTTP <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        response.sendError(400, "Should use POST request to add entries.");
    }

    /**
     * Handles the HTTP <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        PrintWriter respText;
        Journal j = (Journal) this.getServletContext().getAttribute("Journal");
        respText = response.getWriter();
        HttpSession session = request.getSession(false);
        
        if ( ! request.isSecure() || 
                session == null) {
            //Should send error badRequest.
            response.setContentType("text/html;charset=UTF-8");
            response.sendError(400, "Petition isecure or not login.");
            return;
        }
        
        User user = (User) session.getAttribute("user");
        
        String title = null;
        String content = null;
        Calendar date = null;
        byte[] photo = null;
        
        try {
            Map items = new ServletFileUpload(new DiskFileItemFactory()).parseParameterMap(request);
            
            for (Object item : items.values() ) {
                FileItem formItem = (FileItem) item;
                if ( ! FORM_FIELDS.contains(formItem.getFieldName())) {
                    response.setContentType("text/html;charset=UTF-8");
                    response.sendError(400, "Bad form.");
                    return;
                }
                if (formItem.isFormField()) {
                    //TODO: Rewrite better.
                    switch (formItem.getFieldName()) {
                        case "title":
                            title = formItem.getString();
                            break;
                        case "content":
                            content = formItem.getString();
                            break;
                        case "date":
                            date = new GregorianCalendar();
                            date.setTimeInMillis(
                                    Long.valueOf(formItem.getString()));
                            break;
                    }
                }else {
                    //Maybe check image size.
                    photo = formItem.get();
                }
             }
            
        } catch (FileUploadException ex) {
            Logger.getLogger(AddEntry.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        
        
        
        try {
            j.addEntry(user.getId(), title, content, photo);
        } catch (TooManyCharacters ex) {
            Logger.getLogger(AddEntry.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        
    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

}
