package com.aiprous.medicobox.featuredproduct;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class FeaturedProductModel {

    @Expose
    @SerializedName("request_path")
    public String request_path;
    @Expose
    @SerializedName("fulfilled_by")
    public String fulfilled_by;
    @Expose
    @SerializedName("sold_by")
    public String sold_by;
    @Expose
    @SerializedName("availability")
    public String availability;
    @Expose
    @SerializedName("weight_uom")
    public String weight_uom;
    @Expose
    @SerializedName("prescription_required")
    public String prescription_required;
    @Expose
    @SerializedName("product_size")
    public String product_size;
    @Expose
    @SerializedName("product_form")
    public String product_form;
    @Expose
    @SerializedName("marketed_by")
    public String marketed_by;
    @Expose
    @SerializedName("status")
    public String status;
    @Expose
    @SerializedName("warningsprecautionschildrenand")
    public String warningsprecautionschildrenand;
    @Expose
    @SerializedName("warningsprecautionspregnancyan")
    public String warningsprecautionspregnancyan;
    @Expose
    @SerializedName("side_effects")
    public String side_effects;
    @Expose
    @SerializedName("how_to_use")
    public String how_to_use;
    @Expose
    @SerializedName("uses")
    public String uses;
    @Expose
    @SerializedName("composition")
    public String composition;
    @Expose
    @SerializedName("swatch_image")
    public String swatch_image;
    @Expose
    @SerializedName("url_key")
    public String url_key;
    @Expose
    @SerializedName("thumbnail")
    public String thumbnail;
    @Expose
    @SerializedName("small_image")
    public String small_image;
    @Expose
    @SerializedName("image")
    public String image;
    @Expose
    @SerializedName("name")
    public String name;
    @Expose
    @SerializedName("is_salable")
    public String is_salable;
    @Expose
    @SerializedName("cat_index_position")
    public String cat_index_position;
    @Expose
    @SerializedName("sw_featured")
    public String sw_featured;
    @Expose
    @SerializedName("max_price")
    public String max_price;
    @Expose
    @SerializedName("min_price")
    public String min_price;
    @Expose
    @SerializedName("minimal_price")
    public String minimal_price;
    @Expose
    @SerializedName("final_price")
    public String final_price;
    @Expose
    @SerializedName("tax_class_id")
    public String tax_class_id;
    @Expose
    @SerializedName("price")
    public String price;
    @Expose
    @SerializedName("updated_at")
    public String updated_at;
    @Expose
    @SerializedName("created_at")
    public String created_at;
    @Expose
    @SerializedName("required_options")
    public String required_options;
    @Expose
    @SerializedName("has_options")
    public String has_options;
    @Expose
    @SerializedName("sku")
    public String sku;
    @Expose
    @SerializedName("type_id")
    public String type_id;
    @Expose
    @SerializedName("attribute_set_id")
    public String attribute_set_id;
    @Expose
    @SerializedName("entity_id")
    public String entity_id;


    public FeaturedProductModel(String image, String name, String actual_price, String final_price) {
        this.image = image;
        this.name = name;
        this.price = actual_price;
        this.final_price = final_price;
    }

    public String getRequest_path() {
        return request_path;
    }

    public void setRequest_path(String request_path) {
        this.request_path = request_path;
    }

    public String getFulfilled_by() {
        return fulfilled_by;
    }

    public void setFulfilled_by(String fulfilled_by) {
        this.fulfilled_by = fulfilled_by;
    }

    public String getSold_by() {
        return sold_by;
    }

    public void setSold_by(String sold_by) {
        this.sold_by = sold_by;
    }

    public String getAvailability() {
        return availability;
    }

    public void setAvailability(String availability) {
        this.availability = availability;
    }

    public String getWeight_uom() {
        return weight_uom;
    }

    public void setWeight_uom(String weight_uom) {
        this.weight_uom = weight_uom;
    }

    public String getPrescription_required() {
        return prescription_required;
    }

    public void setPrescription_required(String prescription_required) {
        this.prescription_required = prescription_required;
    }

    public String getProduct_size() {
        return product_size;
    }

    public void setProduct_size(String product_size) {
        this.product_size = product_size;
    }

    public String getProduct_form() {
        return product_form;
    }

    public void setProduct_form(String product_form) {
        this.product_form = product_form;
    }

    public String getMarketed_by() {
        return marketed_by;
    }

    public void setMarketed_by(String marketed_by) {
        this.marketed_by = marketed_by;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getWarningsprecautionschildrenand() {
        return warningsprecautionschildrenand;
    }

    public void setWarningsprecautionschildrenand(String warningsprecautionschildrenand) {
        this.warningsprecautionschildrenand = warningsprecautionschildrenand;
    }

    public String getWarningsprecautionspregnancyan() {
        return warningsprecautionspregnancyan;
    }

    public void setWarningsprecautionspregnancyan(String warningsprecautionspregnancyan) {
        this.warningsprecautionspregnancyan = warningsprecautionspregnancyan;
    }

    public String getSide_effects() {
        return side_effects;
    }

    public void setSide_effects(String side_effects) {
        this.side_effects = side_effects;
    }

    public String getHow_to_use() {
        return how_to_use;
    }

    public void setHow_to_use(String how_to_use) {
        this.how_to_use = how_to_use;
    }

    public String getUses() {
        return uses;
    }

    public void setUses(String uses) {
        this.uses = uses;
    }

    public String getComposition() {
        return composition;
    }

    public void setComposition(String composition) {
        this.composition = composition;
    }

    public String getSwatch_image() {
        return swatch_image;
    }

    public void setSwatch_image(String swatch_image) {
        this.swatch_image = swatch_image;
    }

    public String getUrl_key() {
        return url_key;
    }

    public void setUrl_key(String url_key) {
        this.url_key = url_key;
    }

    public String getThumbnail() {
        return thumbnail;
    }

    public void setThumbnail(String thumbnail) {
        this.thumbnail = thumbnail;
    }

    public String getSmall_image() {
        return small_image;
    }

    public void setSmall_image(String small_image) {
        this.small_image = small_image;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getIs_salable() {
        return is_salable;
    }

    public void setIs_salable(String is_salable) {
        this.is_salable = is_salable;
    }

    public String getCat_index_position() {
        return cat_index_position;
    }

    public void setCat_index_position(String cat_index_position) {
        this.cat_index_position = cat_index_position;
    }

    public String getSw_featured() {
        return sw_featured;
    }

    public void setSw_featured(String sw_featured) {
        this.sw_featured = sw_featured;
    }

    public String getMax_price() {
        return max_price;
    }

    public void setMax_price(String max_price) {
        this.max_price = max_price;
    }

    public String getMin_price() {
        return min_price;
    }

    public void setMin_price(String min_price) {
        this.min_price = min_price;
    }

    public String getMinimal_price() {
        return minimal_price;
    }

    public void setMinimal_price(String minimal_price) {
        this.minimal_price = minimal_price;
    }

    public String getFinal_price() {
        return final_price;
    }

    public void setFinal_price(String final_price) {
        this.final_price = final_price;
    }

    public String getTax_class_id() {
        return tax_class_id;
    }

    public void setTax_class_id(String tax_class_id) {
        this.tax_class_id = tax_class_id;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getUpdated_at() {
        return updated_at;
    }

    public void setUpdated_at(String updated_at) {
        this.updated_at = updated_at;
    }

    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }

    public String getRequired_options() {
        return required_options;
    }

    public void setRequired_options(String required_options) {
        this.required_options = required_options;
    }

    public String getHas_options() {
        return has_options;
    }

    public void setHas_options(String has_options) {
        this.has_options = has_options;
    }

    public String getSku() {
        return sku;
    }

    public void setSku(String sku) {
        this.sku = sku;
    }

    public String getType_id() {
        return type_id;
    }

    public void setType_id(String type_id) {
        this.type_id = type_id;
    }

    public String getAttribute_set_id() {
        return attribute_set_id;
    }

    public void setAttribute_set_id(String attribute_set_id) {
        this.attribute_set_id = attribute_set_id;
    }

    public String getEntity_id() {
        return entity_id;
    }

    public void setEntity_id(String entity_id) {
        this.entity_id = entity_id;
    }
}
