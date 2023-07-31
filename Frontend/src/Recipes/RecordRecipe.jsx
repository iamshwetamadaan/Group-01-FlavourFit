import React, { useCallback, useEffect, useState } from "react";
import { useNavigate, useParams } from "react-router-dom";
import { axiosRequest } from "../HttpClients/axiosService";
import { Button, Container, Form, Image } from "react-bootstrap";
import "./recordRecipe.scss";
import addIcon from "../resources/Images/circle-plus-solid.svg";
import closeIcon from "../resources/Images/xmark-solid.svg";

const RecordRecipe = (props) => {
  const { id } = useParams();
  const navigate = useNavigate();

  const [recipe, setRecipe] = useState({
    recipeName: "",
    recipeDescription: "",
    types: "",
  });
  const [ingredients, setIngredients] = useState([
    { ingredientName: "", quantity: "", quantityUnit: "" },
  ]);
  const [validated, setValidated] = useState(false);
  const [error, setError] = useState("");

  useEffect(() => {
    let recipeId = parseInt(id);
    if (recipeId !== 0) {
      axiosRequest(
        {
          url: "/recipes/get-by-id",
          params: { recipeId: recipeId },
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
    }
  }, [id]);

  const addRecipe = useCallback(
    (e) => {
      e.preventDefault();
      const form = e.currentTarget;
      if (form.checkValidity() === false) {
        e.stopPropagation();
      } else {
        debugger;
        setValidated(true);
        const completeRecipe = {
          recipe: { ...recipe },
          ingredients: [...ingredients],
        };

        axiosRequest(
          {
            url: recipe?.recipeId ? "/recipes/update" : "/recipes/record",
            method: recipe?.recipeId ? "PUT" : "POST",
            data: { ...completeRecipe },
          },
          (response) => {
            if (response?.data?.data?.recipe) {
              let recipeId = response.data.data.recipe.recipeId ?? 0;
              navigate(`/recipes/${recipeId}`);
            }
          },
          (error) => {
            setError(error?.data?.data?.message ?? "Failed to record recipe");
          }
        );
      }
    },
    [recipe, ingredients]
  );

  const handleRecipeChange = (e) => {
    setRecipe((prevState) => {
      let newState = { ...prevState };
      newState = { ...newState, [e.target.name]: e.target.value };
      return newState;
    });
  };

  const handleIngredientChange = (e) => {
    let index = parseInt(e.target.getAttribute("ingredientindex"));
    setIngredients((prevState) => {
      let newState = [...prevState];
      if (newState[index]) {
        newState[index][e.target.name] = e.target.value;
      }
      return newState;
    });
  };

  const removeIngredient = (index) => {
    setIngredients((prevState) => {
      let newState = [...prevState];
      if (newState[index]) {
        newState.splice(index, 1);
      }
      return newState;
    });
  };

  const addIngredient = (e) => {
    setIngredients((prevState) => {
      let newState = [...prevState];
      newState.push({ ingredientName: "", quantity: "", quantityUnit: "" });
      return newState;
    });
  };

  return (
    <Container className="ff-record-recipe">
      <h4 style={{ marginBottom: "20px" }}>
        {recipe?.recipeId ? "Edit Recipe" : "Add Recipe"}
      </h4>
      <Form validated={validated} onSubmit={addRecipe}>
        <Form.Group className="record-recipe-form-group">
          <Form.Label>Recipe name</Form.Label>
          <Form.Control
            required
            type="text"
            name="recipeName"
            placeholder="Recipe name"
            value={recipe?.recipeName ?? ""}
            onChange={handleRecipeChange}
          />
        </Form.Group>
        <Form.Group className="record-recipe-form-group">
          <Form.Label>Recipe description</Form.Label>
          <Form.Control
            as="textarea"
            rows={3}
            name="recipeDescription"
            value={recipe?.recipeDescription ?? ""}
            onChange={handleRecipeChange}
          />
        </Form.Group>
        <Form.Group className="record-recipe-form-group">
          <Form.Label>Type</Form.Label>
          <Form.Control
            required
            type="text"
            name="types"
            placeholder="Type of cusine/recipe"
            value={recipe?.types ?? ""}
            onChange={handleRecipeChange}
          />
        </Form.Group>
        {ingredients?.length > 0 ? (
          <div className="add-ingredients">
            <h5 style={{ marginBottom: "20px" }}>Ingredients</h5>
            {ingredients.map((ingredient, index) => {
              return (
                <div className="ingredients-row">
                  <Form.Group className="record-ingredient-form-group">
                    {index === 0 ? <Form.Label>Name</Form.Label> : null}
                    <Form.Control
                      required
                      type="text"
                      name="ingredientName"
                      ingredientindex={index}
                      placeholder="Ingredient name"
                      value={ingredient.ingredientName ?? ""}
                      onChange={handleIngredientChange}
                    />
                  </Form.Group>
                  <Form.Group className="record-ingredient-form-group">
                    {index === 0 ? <Form.Label>Quantity</Form.Label> : null}
                    <Form.Control
                      required
                      type="number"
                      name="quantity"
                      ingredientindex={index}
                      placeholder="Ingredient quantity"
                      value={ingredient.quantity ?? ""}
                      onChange={handleIngredientChange}
                    />
                  </Form.Group>
                  <Form.Group className="record-ingredient-form-group">
                    {index === 0 ? <Form.Label>Unit</Form.Label> : null}
                    <Form.Control
                      required
                      type="text"
                      name="quantityUnit"
                      ingredientindex={index}
                      placeholder="Quantity unit (mL,g...)"
                      value={ingredient.quantityUnit ?? ""}
                      onChange={handleIngredientChange}
                    />
                  </Form.Group>
                  {index !== 0 ? (
                    <Image
                      src={closeIcon}
                      onClick={(e) => removeIngredient(index)}
                      width={15}
                      style={{ cursor: "pointer" }}
                    />
                  ) : (
                    <div style={{ width: "15px" }}></div>
                  )}
                </div>
              );
            })}
          </div>
        ) : null}

        <div className="add-ingredient-container" onClick={addIngredient}>
          <Image src={addIcon} width={20} />
          Add ingredient
        </div>

        <div className="record-recipe-btn-container">
          <Button type="submit">Submit</Button>
          <Button
            variant="outline-primary"
            type="button"
            className="record-recipe-cancel-btn"
            onClick={(e) => {
              if (id !== 0) {
                navigate(`/recipes/${id}`);
              } else {
                navigate("/recipes");
              }
            }}
          >
            Cancel
          </Button>
        </div>
      </Form>
    </Container>
  );
};

export default RecordRecipe;
