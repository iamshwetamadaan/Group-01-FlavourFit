import React, { useCallback, useEffect, useState } from "react";
import "./recipeDetails.scss";
import { useNavigate, useParams } from "react-router-dom";
import { axiosRequest } from "../HttpClients/axiosService";
import { Container, Image } from "react-bootstrap";
import editIcon from "../resources/Images/pen-to-square-solid.svg";
import saveIcon from "../resources/Images/bookmark-regular.svg";
import savedIcon from "../resources/Images/bookmark-solid.svg";
import shareIcon from "../resources/Images/share-nodes-solid.svg";

const RecipeDetails = (props) => {
  const { id } = useParams();
  const navigate = useNavigate();

  const [recipe, setRecipe] = useState(null);
  const [ingredients, setIngredients] = useState([]);
  const [saved, setSaved] = useState(false);

  useEffect(() => {
    axiosRequest(
      {
        url: "/recipes/get-by-id",
        params: { recipeId: id },
      },
      (response) => {
        if (response?.data?.data) {
          const recipeData = { ...response.data.data };
          setRecipe(recipeData?.recipe ?? null);
          setIngredients(recipeData?.ingredients ?? []);
        } else {
          navigate("/recipes");
        }
      },
      (error) => {
        navigate("/recipes");
      }
    );
  }, [id]);

  useEffect(() => {
    if (!saved) {
      axiosRequest(
        {
          url: "/recipes/saved-recipes",
          method: "get",
          params: {
            count: 100,
          },
        },
        (response) => {
          if (response?.data?.data) {
            let savedRecipes = [...response.data.data];
            if (
              savedRecipes.filter((recipe) => recipe.recipeId === parseInt(id))
                .length > 0
            ) {
              setSaved(true);
            }
          }
        },
        (error) => {}
      );
    }
  }, []);

  const handleSave = useCallback(
    (e) => {
      axiosRequest(
        {
          url: "/recipes/save-recipe",
          method: "POST",
          params: { recipeId: id },
        },
        (response) => {
          setSaved(true);
        },
        (error) => {}
      );
    },
    [id]
  );

  const handleShare = useCallback(
    (e) => {
      axiosRequest(
        {
          url: "/feeds/post-recipe",
          method: "PUT",
          params: { recipeId: id },
        },
        (response) => {
          const feedId = response?.data?.data?.feedId ?? 0;
        },
        (error) => {}
      );
    },
    [id]
  );

  if (recipe == null) {
    return null;
  }

  return (
    <div className="ff-recipe-details">
      <Container className="recipe-container">
        <div className="recipe-type">
          {recipe?.types ?? ""}{" "}
          {recipe?.editable ? (
            <Image
              src={editIcon}
              onClick={(e) => {
                navigate(`/recipes/record/${id}`);
              }}
              width={30}
            />
          ) : null}
        </div>
        <div className="recipe-name">
          {recipe?.recipeName ?? ""}{" "}
          {saved ? (
            <Image src={savedIcon} width={25} />
          ) : (
            <Image src={saveIcon} onClick={handleSave} width={25} />
          )}
        </div>
        <div className="placeholder-line">
          <Image src={shareIcon} width={25} onClick={handleShare} />
        </div>

        {ingredients?.length > 0 ? (
          <div className="recipe-ingredients">
            <div className="ingredients-title">Ingredients</div>
            {ingredients.map((item, index) => {
              return (
                <div className="ingredient">
                  <div className="ingredient-name">{item.ingredientName}</div>
                  <div className="ingredient-quantity">{item.quantity}</div>
                  <div className="ingredient-quantity-unit">
                    {item.quantityUnit}
                  </div>
                </div>
              );
            })}
          </div>
        ) : null}

        <div className="recipe-desc-title">Description</div>
        <div className="recipe-desc">{recipe?.recipeDescription ?? ""}</div>
      </Container>
    </div>
  );
};

export default RecipeDetails;
