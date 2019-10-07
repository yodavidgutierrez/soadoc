<%@ tag description="input text - bootstrap" pageEncoding="UTF-8" %>
<%@ attribute name="id" required="true" %>
<%@ attribute name="tabindex" required="true" %>
<%@ attribute name="label" required="true" %>
<div class="form-group">
    <label class="control-label col-md-12"
           for="${id}">${label}</label>

    <div class="col-md-12">
        <label>
            <input type="radio" name="${id}" id="${id}checkboxSi"
                   tabindex="${tabindex}"
                   value="1" checked>
            Si
        </label>
        <label>
            <input type="radio" name="${id}" id="${id}checkboxNo"
                   value="0">
            No
        </label>
    </div>
</div>